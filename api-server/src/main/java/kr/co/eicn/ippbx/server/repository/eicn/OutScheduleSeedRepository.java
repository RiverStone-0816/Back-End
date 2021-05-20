package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.OutScheduleSeed;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.OutScheduleListRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.OutScheduleSeedRecord;
import kr.co.eicn.ippbx.model.entity.eicn.OutScheduleListEntity;
import kr.co.eicn.ippbx.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.form.DayOutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.model.form.OutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.model.search.OutScheduleSeedSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.EicnUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.OutScheduleList.OUT_SCHEDULE_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.OutScheduleSeed.OUT_SCHEDULE_SEED;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class OutScheduleSeedRepository extends EicnBaseRepository<OutScheduleSeed, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(OutScheduleSeedRepository.class);

	private final SoundListRepository soundListRepository;
	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;

	public OutScheduleSeedRepository(SoundListRepository soundListRepository, CacheService cacheService, PBXServerInterface pbxServerInterface) {
		super(OUT_SCHEDULE_SEED, OUT_SCHEDULE_SEED.PARENT, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed.class);
		this.soundListRepository = soundListRepository;
		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
	}

	public Integer nextSequence() {
		final OutScheduleSeed sequenceSeed = OUT_SCHEDULE_SEED.as("SEQUENCE_SEED");
		return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.PARENT), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
	}

	public List<OutScheduleSeedEntity> getScheduleLists(OutScheduleSeedSearchRequest search) {
		final SelectConditionStep<Record> query = dsl.select()
				.from(OUT_SCHEDULE_SEED)
				.leftOuterJoin(OUT_SCHEDULE_LIST)
				.on(OUT_SCHEDULE_SEED.PARENT.eq(OUT_SCHEDULE_LIST.PARENT).and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId())))
				.where(compareCompanyId());

		if (isNotEmpty(search.getName()))
			query.and(OUT_SCHEDULE_SEED.NAME.like("%" + search.getName() + "%"));
		if (isNotEmpty(search.getWeek()))
			query.and(OUT_SCHEDULE_LIST.WEEK.eq(search.getWeek()));

		query.and(OUT_SCHEDULE_SEED.TYPE.eq(search.getType().getCode()));

		final Map<OutScheduleSeedRecord, Result<Record>> recordResultMap = query.orderBy(OUT_SCHEDULE_SEED.NAME, OUT_SCHEDULE_LIST.EXTENSION)
				.fetch()
				.intoGroups(OUT_SCHEDULE_SEED);

		final List<OutScheduleSeedEntity> outScheduleSeedEntities = new ArrayList<>();
		final Map<Integer, String> soundListMap = soundListRepository.findAll().stream().collect(Collectors.toMap(SoundList::getSeq, SoundList::getSoundName));

		recordResultMap.forEach((record, records) -> {
			final OutScheduleSeedEntity entity = record.into(OUT_SCHEDULE_SEED).into(OutScheduleSeedEntity.class);
			entity.setOutScheduleLists(records.stream()
			.filter(r -> r.getValue(OUT_SCHEDULE_LIST.CODE) != null)
			.map(r -> {
				final OutScheduleListEntity into = r.into(OUT_SCHEDULE_LIST).into(OutScheduleListEntity.class);
				into.setSoundName(isNotEmpty(into.getSoundcode()) ? soundListMap.getOrDefault(Integer.valueOf(into.getSoundcode()), "음원지정안함") : "음원지정안함");
				return into;
			})
			.collect(Collectors.toList()));

			outScheduleSeedEntities.add(entity);
		});

		return outScheduleSeedEntities;
	}

	public void insertOnWeekScheduleAllPbxServers(OutScheduleSeedFormRequest form) {
		final OutScheduleSeed sequenceSeed = OUT_SCHEDULE_SEED.as("SEQUENCE_SEED");
		final Integer sequenceKey = dsl.select(DSL.ifnull(DSL.max(sequenceSeed.PARENT), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed();
		ReflectionUtils.copy(record, form);

		record.setParent(sequenceKey);
		record.setCompanyId(getCompanyId());
		record.setType(ScheduleType.WEEK.getCode());

		this.insert(record);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.insert(pbxDsl, record);
					}
				});

		final OutScheduleList outScheduleListRecord = new OutScheduleList();
		outScheduleListRecord.setParent(sequenceKey);
		outScheduleListRecord.setType(ScheduleType.WEEK.getCode());
		outScheduleListRecord.setName(form.getName());
		outScheduleListRecord.setFromhour(form.getFromhour());
		outScheduleListRecord.setTohour(form.getTohour());
		outScheduleListRecord.setFromtime(Timestamp.valueOf("2007-11-22 00:00:00"));
		outScheduleListRecord.setTotime(Timestamp.valueOf("2007-11-22 00:00:00"));
		outScheduleListRecord.setSoundcode(form.getSoundCode());
		outScheduleListRecord.setCompanyId(getCompanyId());

		for (String week : form.getWeeks()) {
			outScheduleListRecord.setWeek(week);
			for (String extension : form.getExtensions()) {
				outScheduleListRecord.setExtension(extension);
				outScheduleListRecord.setCode(dsl.select(DSL.ifnull(DSL.max(OUT_SCHEDULE_LIST.CODE), 0).add(1)).from(OUT_SCHEDULE_LIST).fetchOneInto(Integer.class));

				dsl.insertInto(OUT_SCHEDULE_LIST)
						.set(dsl.newRecord(OUT_SCHEDULE_LIST, outScheduleListRecord))
						.execute();

				cacheService.pbxServerList(getCompanyId())
						.forEach(e -> {
							try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
								pbxDsl.insertInto(OUT_SCHEDULE_LIST)
										.set(pbxDsl.newRecord(OUT_SCHEDULE_LIST, outScheduleListRecord))
										.execute();
							}
						});
			}
		}
	}

	public void insertOnDayScheduleAllPbxServers(DayOutScheduleSeedFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed();

		final OutScheduleSeed sequenceSeed = OUT_SCHEDULE_SEED.as("SEQUENCE_SEED");
		final Integer sequenceKey = dsl.select(DSL.ifnull(DSL.max(sequenceSeed.PARENT), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
		ReflectionUtils.copy(record, form);

		record.setParent(sequenceKey);
		record.setCompanyId(getCompanyId());
		record.setType(ScheduleType.DAY.getCode());

		this.insert(record);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.insert(pbxDsl, record);
					}
				});

		final OutScheduleListRecord outScheduleListRecord = new OutScheduleListRecord();
		outScheduleListRecord.setParent(sequenceKey);
		outScheduleListRecord.setType(ScheduleType.DAY.getCode());
		outScheduleListRecord.setName(form.getName());
		outScheduleListRecord.setFromhour(form.getFromhour());
		outScheduleListRecord.setTohour(form.getTohour());
		outScheduleListRecord.setSoundcode(defaultString(form.getSoundCode()));
		outScheduleListRecord.setCompanyId(getCompanyId());

		final List<LocalDate> localDates = EicnUtils.betweenDate(LocalDate.parse(form.getFromDate()), LocalDate.parse(form.getToDate()));

		for (LocalDate localDate : localDates) {
			outScheduleListRecord.setFromtime(Timestamp.valueOf(localDate.toString() + " 00:00:00"));
			outScheduleListRecord.setTotime(Timestamp.valueOf(localDate.toString() + " 23:59:59"));

			for (String extension : form.getExtensions()) {
				outScheduleListRecord.setExtension(extension);
				outScheduleListRecord.setCode(dsl.select(DSL.ifnull(DSL.max(OUT_SCHEDULE_LIST.CODE), 0).add(1)).from(OUT_SCHEDULE_LIST).fetchOneInto(Integer.class));

				dsl.insertInto(OUT_SCHEDULE_LIST)
						.set(outScheduleListRecord)
						.execute();

				cacheService.pbxServerList(getCompanyId())
						.forEach(e -> {
							try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
								pbxDsl.insertInto(OUT_SCHEDULE_LIST)
										.set(outScheduleListRecord)
										.execute();
							}
						});
			}
		}
	}

	public void updateAllPbxServers(OutScheduleSeedFormRequest form, Integer parent) {
		this.updateByKey(form, parent);

		dsl.deleteFrom(OUT_SCHEDULE_LIST)
				.where(OUT_SCHEDULE_LIST.PARENT.eq(parent))
				.and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId()))
				.execute();

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.updateByKey(form, parent);

						pbxDsl.deleteFrom(OUT_SCHEDULE_LIST)
								.where(OUT_SCHEDULE_LIST.PARENT.eq(parent))
								.and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId()))
								.execute();
					}
				});

		final OutScheduleListRecord outScheduleListRecord = new OutScheduleListRecord();
		outScheduleListRecord.setParent(parent);
		outScheduleListRecord.setType(ScheduleType.WEEK.getCode());
		outScheduleListRecord.setName(form.getName());
		outScheduleListRecord.setFromhour(form.getFromhour());
		outScheduleListRecord.setTohour(form.getTohour());
		outScheduleListRecord.setFromtime(Timestamp.valueOf("2007-11-22 00:00:00"));
		outScheduleListRecord.setTotime(Timestamp.valueOf("2007-11-22 00:00:00"));
		outScheduleListRecord.setSoundcode(form.getSoundCode());
		outScheduleListRecord.setCompanyId(getCompanyId());

		for (String week : form.getWeeks()) {
			outScheduleListRecord.setWeek(week);
			for (String extension : form.getExtensions()) {
				outScheduleListRecord.setExtension(extension);
				outScheduleListRecord.setCode(dsl.select(DSL.ifnull(DSL.max(OUT_SCHEDULE_LIST.CODE), 0).add(1)).from(OUT_SCHEDULE_LIST).fetchOneInto(Integer.class));

				dsl.insertInto(OUT_SCHEDULE_LIST)
						.set(outScheduleListRecord)
						.execute();

				cacheService.pbxServerList(getCompanyId())
						.forEach(e -> {
							try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
								pbxDsl.insertInto(OUT_SCHEDULE_LIST)
										.set(outScheduleListRecord)
										.execute();
							}
						});
			}
		}
	}

	public void updateOnDayScheduleAllPbxServers(DayOutScheduleSeedFormRequest form, Integer parent) {
		this.updateByKey(form, parent);

		dsl.deleteFrom(OUT_SCHEDULE_LIST)
				.where(OUT_SCHEDULE_LIST.PARENT.eq(parent))
				.and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId()))
				.execute();

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.updateByKey(form, parent);

						pbxDsl.deleteFrom(OUT_SCHEDULE_LIST)
								.where(OUT_SCHEDULE_LIST.PARENT.eq(parent))
								.and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId()))
								.execute();
					}
				});

		final OutScheduleListRecord outScheduleListRecord = new OutScheduleListRecord();
		outScheduleListRecord.setParent(parent);
		outScheduleListRecord.setType(ScheduleType.WEEK.getCode());
		outScheduleListRecord.setName(form.getName());
		outScheduleListRecord.setFromhour(form.getFromhour());
		outScheduleListRecord.setTohour(form.getTohour());
		outScheduleListRecord.setSoundcode(defaultString(form.getSoundCode()));
		outScheduleListRecord.setCompanyId(getCompanyId());

		final List<LocalDate> localDates = EicnUtils.betweenDate(LocalDate.parse(form.getFromDate()), LocalDate.parse(form.getToDate()));

		for (LocalDate localDate : localDates) {
			outScheduleListRecord.setFromtime(Timestamp.valueOf(localDate.toString() + " 00:00:00"));
			outScheduleListRecord.setTotime(Timestamp.valueOf(localDate.toString() + " 23:59:59"));
			for (String extension : form.getExtensions()) {
				outScheduleListRecord.setExtension(extension);
				outScheduleListRecord.setCode(dsl.select(DSL.ifnull(DSL.max(OUT_SCHEDULE_LIST.CODE), 0).add(1)).from(OUT_SCHEDULE_LIST).fetchOneInto(Integer.class));

				dsl.insertInto(OUT_SCHEDULE_LIST)
						.set(outScheduleListRecord)
						.execute();

				cacheService.pbxServerList(getCompanyId())
						.forEach(e -> {
							try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
								pbxDsl.insertInto(OUT_SCHEDULE_LIST)
										.set(outScheduleListRecord)
										.execute();
							}
						});
			}
		}
	}

	public void deleteAllPbxServers(Integer parent) {
		super.deleteOnIfNullThrow(parent);

		dsl.deleteFrom(OUT_SCHEDULE_LIST)
				.where(OUT_SCHEDULE_LIST.PARENT.eq(parent))
				.and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId()))
				.execute();

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						super.delete(pbxDsl, parent);

						pbxDsl.deleteFrom(OUT_SCHEDULE_LIST)
								.where(OUT_SCHEDULE_LIST.PARENT.eq(parent))
								.and(OUT_SCHEDULE_LIST.COMPANY_ID.eq(getCompanyId()))
								.execute();
					}
				});
	}
}
