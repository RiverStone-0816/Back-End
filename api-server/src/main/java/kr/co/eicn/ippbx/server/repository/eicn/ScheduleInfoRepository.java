package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ScheduleInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.ScheduleInfoRecord;
import kr.co.eicn.ippbx.server.model.enums.ScheduleType;
import kr.co.eicn.ippbx.server.model.form.*;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.EicnUtils;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ScheduleInfo.SCHEDULE_INFO;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.jooq.impl.DSL.*;

@Getter
@Repository
public class ScheduleInfoRepository extends EicnBaseRepository<ScheduleInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(ScheduleInfoRepository.class);
	private final CompanyTreeRepository companyTreeRepository;
	private final Number070Repository numberRepository;
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	private final int MAX_PERIOD = 20;

	public ScheduleInfoRepository(CompanyTreeRepository companyTreeRepository, Number070Repository numberRepository, PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(SCHEDULE_INFO, SCHEDULE_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo.class);
		this.companyTreeRepository = companyTreeRepository;
		this.numberRepository = numberRepository;
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;

		orderByFields.add(SCHEDULE_INFO.FROMDATE.desc());
		orderByFields.add(SCHEDULE_INFO.NUMBER.asc());
	}

	public void insertOnWeekSchedule(ScheduleInfoFormRequest form) {
		final List<String> weeks = Arrays.asList("1Mon", "2Tue", "3Wed", "4Thu", "5Fri", "6Sat", "7Sun");
		final ScheduleInfoRecord record = new ScheduleInfoRecord();

		record.setType(ScheduleType.WEEK.getCode());
		record.setFromdate(Date.valueOf("2007-11-22"));
		record.setTodate(Date.valueOf("2007-11-22"));
		record.setCompanyId(getCompanyId());
		record.setGroupId(form.getGroupId());

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				record.setGroupCode(form.getGroupCode());
				record.setGroupTreeName(companyTree.getGroupTreeName());
				record.setGroupLevel(companyTree.getGroupLevel());
			}
		}

		final List<String> scheduleNumbers = dsl.selectDistinct(SCHEDULE_INFO.NUMBER)
				.from(SCHEDULE_INFO)
				.where(compareCompanyId())
				.and(SCHEDULE_INFO.TYPE.eq(ScheduleType.WEEK.getCode()))
				.fetchInto(String.class);

		form.getNumbers().removeIf(e -> scheduleNumbers.stream().anyMatch(e::equals));

		for (String week : weeks) {
			record.setWeek(week);

			for (String number: form.getNumbers()) {
				record.setNumber(number);

				final ScheduleInfoRecord pbxRecord = insertOnGeneratedKey(record).into(new ScheduleInfoRecord());
				pbxRecord.changed(true);

				cacheService.pbxServerList(getCompanyId()).forEach(e -> {
							try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
								insert(pbxDsl, pbxRecord);
							}
						});
			}
		}
	}

	public void insertOnDaySchedule(DayScheduleInfoFormRequest form) {
		final List<LocalDate> localDates = EicnUtils.betweenDate(LocalDate.parse(form.getFromDate()), LocalDate.parse(form.getToDate()));
		if (localDates.size() > MAX_PERIOD)
			throw new IllegalArgumentException(String.format("기간은 %d일 이내여야 합니다.", MAX_PERIOD));

		final ScheduleInfoRecord record = new ScheduleInfoRecord();

		record.setType(ScheduleType.DAY.getCode());
		record.setCompanyId(getCompanyId());
		record.setGroupId(form.getGroupId());

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				record.setGroupCode(form.getGroupCode());
				record.setGroupTreeName(companyTree.getGroupTreeName());
				record.setGroupLevel(companyTree.getGroupLevel());
			}
		}

		final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo> dayScheduleInfos = findAll(SCHEDULE_INFO.TYPE.eq(ScheduleType.DAY.getCode()));

		for (LocalDate localDate : localDates) {
			record.setFromdate(Date.valueOf(localDate));
			record.setTodate(Date.valueOf(localDate));
			for (String number: form.getNumbers()) {
				if (dayScheduleInfos.stream().noneMatch(e -> e.getNumber().equals(number) && Date.valueOf(localDate).equals(e.getFromdate()))) {
					record.setNumber(number);

					final ScheduleInfoRecord pbxRecord = insertOnGeneratedKey(record).into(new ScheduleInfoRecord());
					pbxRecord.changed(true);

					cacheService.pbxServerList(getCompanyId()).forEach(e -> {
						try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
							insert(pbxDsl, pbxRecord);
						}
					});
				}
			}
		}
	}

	public void insertOnDayHolySchedule(HolyScheduleInfoFormRequest form) {
		final List<PeriodDateFormRequest> periodDates = form.getPeriodDates();

		for (PeriodDateFormRequest periodDate : periodDates) {
			final DayScheduleInfoFormRequest formRequest = new DayScheduleInfoFormRequest();
			ReflectionUtils.copy(formRequest, form);
			formRequest.setFromDate(periodDate.getFromDate());
			formRequest.setToDate(periodDate.getToDate());

			insertOnDaySchedule(formRequest);
		}
	}

	public void updateByWeekType(ScheduleInfoUpdateFormRequest form, Integer key) {
		final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo record = findOne(key);
		final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());

		if (companyTree != null) {
			record.setGroupCode(companyTree.getGroupCode());
			record.setGroupTreeName(companyTree.getGroupTreeName());
			record.setGroupLevel(companyTree.getGroupLevel());
		}
		record.setGroupId(form.getGroupId());

		updateByKey(record, key);

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
				updateByKey(pbxDsl, record, key);
			}
		});
	}

	public void updateByDayType(DayScheduleInfoUpdateFormRequest form, Integer key) {
		final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo record = findOneIfNullThrow(key);

		record.setFromdate(Date.valueOf(form.getFromDate()));
		record.setTodate(Date.valueOf(form.getFromDate()));
		record.setGroupId(form.getGroupId());

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				record.setGroupCode(companyTree.getGroupCode());
				record.setGroupLevel(companyTree.getGroupLevel());
				record.setGroupTreeName(companyTree.getGroupTreeName());
			}
		}

		update(dsl, record, SCHEDULE_INFO.SEQ.eq(key));

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
				update(pbxDsl, record, SCHEDULE_INFO.SEQ.eq(key));
			}
		});
	}

	public void deleteAllPBXServer(Integer seq) {
		delete(seq);
		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
			try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
				delete(pbxDsl, seq);
			}
		});
	}

	public void deleteByNumber(ScheduleType type, String number) {
		numberRepository.findOneIfNullThrow(number);

		delete(SCHEDULE_INFO.NUMBER.eq(number).and(SCHEDULE_INFO.TYPE.eq(type.getCode())));

		cacheService.pbxServerList(getCompanyId()).forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						delete(pbxDsl, SCHEDULE_INFO.NUMBER.eq(number).and(SCHEDULE_INFO.TYPE.eq(type.getCode())));
					}
				});
	}

	public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo getScheduleByService(String serviceNumber) {
		final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo> scheduleInfo = findAll(SCHEDULE_INFO.TYPE.eq(ScheduleType.DAY.getCode()).and(SCHEDULE_INFO.NUMBER.eq(serviceNumber)).and(SCHEDULE_INFO.FROMDATE.eq(currentDate()))).stream().findFirst();

		if (scheduleInfo.isPresent())
			return scheduleInfo.get();
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.get(Calendar.DAY_OF_WEEK);
			return findAll(SCHEDULE_INFO.TYPE.eq(ScheduleType.WEEK.getCode()).and(SCHEDULE_INFO.NUMBER.eq(serviceNumber).and(SCHEDULE_INFO.WEEK.like("%" + (calendar.get(Calendar.DAY_OF_WEEK)-1) + "%")))).stream().findFirst().orElse(null);
		}
	}
}
