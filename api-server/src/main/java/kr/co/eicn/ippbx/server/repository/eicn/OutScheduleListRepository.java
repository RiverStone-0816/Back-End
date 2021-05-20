package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.OutScheduleList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HolyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.OutScheduleListRecord;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.form.DayScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.HolyOutScheduleFormRequest;
import kr.co.eicn.ippbx.model.form.PeriodDateFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.EicnUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.OutScheduleList.OUT_SCHEDULE_LIST;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class OutScheduleListRepository extends EicnBaseRepository<OutScheduleList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(OutScheduleListRepository.class);

	private final OutScheduleSeedRepository outScheduleSeedRepository;
	private final HolyInfoRepository holyInfoRepository;
	private final CompanyTreeRepository companyTreeRepository;
	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;

	private final int MAX_PERIOD = 20;

	public OutScheduleListRepository(OutScheduleSeedRepository outScheduleSeedRepository, HolyInfoRepository holyInfoRepository, CompanyTreeRepository companyTreeRepository, CacheService cacheService, PBXServerInterface pbxServerInterface) {
		super(OUT_SCHEDULE_LIST, OUT_SCHEDULE_LIST.CODE, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleList.class);
		this.outScheduleSeedRepository = outScheduleSeedRepository;
		this.holyInfoRepository = holyInfoRepository;
		this.companyTreeRepository = companyTreeRepository;
		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
	}

	public void insertOnDayHolySchedule(HolyOutScheduleFormRequest form) {
		final List<HolyInfo> holyInfos = holyInfoRepository.findAll().stream().sorted(Comparator.comparing(HolyInfo::getHolyDate)).collect(Collectors.toList());
		final List<PeriodDateFormRequest> periodDates = form.getPeriodDates();

		int i = 0;
		for (PeriodDateFormRequest periodDate : periodDates) {
			final DayScheduleInfoFormRequest formRequest = new DayScheduleInfoFormRequest();
			ReflectionUtils.copy(formRequest, form);
			formRequest.setFromDate(periodDate.getFromDate());
			formRequest.setToDate(periodDate.getToDate());

			insertOnDaySchedule(formRequest, holyInfos.get(i).getHolyName(), form.getSoundCode());
			i++;
		}
	}

	public void insertOnDaySchedule(DayScheduleInfoFormRequest form, String holyName, String soundCode) {
		final List<LocalDate> localDates = EicnUtils.betweenDate(LocalDate.parse(form.getFromDate()), LocalDate.parse(form.getToDate()));
		if (localDates.size() > MAX_PERIOD)
			throw new IllegalArgumentException(String.format("기간은 %d일 이내여야 합니다.", MAX_PERIOD));

		final OutScheduleSeed seedRecord = new OutScheduleSeed();
		final Integer sequenceKey = outScheduleSeedRepository.nextSequence();
		ReflectionUtils.copy(seedRecord, form);

		seedRecord.setParent(sequenceKey);
		seedRecord.setName(holyName);
		seedRecord.setCompanyId(getCompanyId());
		seedRecord.setType(ScheduleType.DAY.getCode());

		outScheduleSeedRepository.insertOnGeneratedKey(seedRecord);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						outScheduleSeedRepository.insertOnGeneratedKey(pbxDsl, seedRecord);
					}
				});

		final OutScheduleListRecord record = new OutScheduleListRecord();

		record.setType(ScheduleType.DAY.getCode());
		record.setCompanyId(getCompanyId());
		record.setParent(sequenceKey);
		record.setSoundcode(soundCode);

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null)
				record.setExtension(companyTree.getGroupTreeName());
		}

		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleList> dayScheduleInfos = findAll(OUT_SCHEDULE_LIST.TYPE.eq(ScheduleType.DAY.getCode()));

		for (LocalDate localDate : localDates) {
			record.setName(holyName);
			record.setCode(dsl.select(DSL.ifnull(DSL.max(OUT_SCHEDULE_LIST.CODE), 0).add(1)).from(OUT_SCHEDULE_LIST).fetchOneInto(Integer.class));
			record.setFromtime(Timestamp.valueOf(Date.valueOf(localDate) + " 00:00:00"));
			record.setTotime(Timestamp.valueOf(Date.valueOf(localDate) + " 23:59:59"));
			record.setFromhour(0);
			record.setTohour(23 * 60 + 59);

			if (dayScheduleInfos.stream().noneMatch(e -> e.getExtension().equals(record.getExtension()) && record.getFromtime().equals(e.getFromtime()))) {
				this.insertOnGeneratedKey(record);

				cacheService.pbxServerList(getCompanyId()).forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.insert(pbxDsl, record);
					}
				});
			}
		}
	}
}
