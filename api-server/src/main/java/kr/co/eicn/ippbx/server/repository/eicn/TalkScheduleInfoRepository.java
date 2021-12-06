package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleInfoEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.form.DayTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.HolyTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PeriodDateFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.EicnUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleGroup.TALK_SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleInfo.TALK_SCHEDULE_INFO;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class TalkScheduleInfoRepository extends EicnBaseRepository<TalkScheduleInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkScheduleInfoRepository.class);
	private final CompanyTreeRepository companyTreeRepository;
	private final TalkScheduleGroupRepository talkScheduleGroupRepository;
	private final OrganizationService organizationService;

	public TalkScheduleInfoRepository(CompanyTreeRepository companyTreeRepository, TalkScheduleGroupRepository talkScheduleGroupRepository, OrganizationService organizationService) {
		super(TALK_SCHEDULE_INFO, TALK_SCHEDULE_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo.class);
		this.companyTreeRepository = companyTreeRepository;
		this.talkScheduleGroupRepository = talkScheduleGroupRepository;
		this.organizationService = organizationService;
	}

	public List<TalkScheduleInfoEntity> getTalkScheduleInfoLists() {
		return dsl.select()
				.from(TALK_SCHEDULE_INFO)
				.innerJoin(TALK_SCHEDULE_GROUP)
				.on(TALK_SCHEDULE_INFO.GROUP_ID.eq(TALK_SCHEDULE_GROUP.PARENT))
				.where(compareCompanyId())
				.orderBy(TALK_SCHEDULE_GROUP.NAME)
				.fetch(record -> {
					final TalkScheduleInfoEntity entity = record.into(TALK_SCHEDULE_INFO).into(TalkScheduleInfoEntity.class);
					entity.setScheduleGroup(record.into(TALK_SCHEDULE_GROUP).into(TalkScheduleGroupEntity.class));
					return entity;
				});
	}

	public void insertOnWeekSchedule(TalkScheduleInfoFormRequest form) {
		final List<String> weeks = Arrays.asList("1Mon", "2Tue", "3Wed", "4Thu", "5Fri", "6Sat", "7Sun");
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo();

		record.setType(ScheduleType.WEEK.getCode());
		record.setFromdate(Date.valueOf("2007-11-22"));
		record.setTodate(Date.valueOf("2007-11-22"));
		record.setGroupId(form.getGroupId());
		record.setCompanyId(getCompanyId());
		record.setGroupCode(form.getGroupCode());
		record.setIsStat(EMPTY);
		record.setChannelType(form.getChannelType().getCode());

		for (String week : weeks) {
			record.setWeek(week);
			for (String senderKey : form.getSenderKeys()) {
				record.setSenderKey(senderKey);
				super.insert(record);
			}
		}
	}

	public void insertOnDaySchedule(DayTalkScheduleInfoFormRequest form) {
		final List<LocalDate> localDates = EicnUtils.betweenDate(LocalDate.parse(form.getFromDate()), LocalDate.parse(form.getToDate()));
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo();

		record.setType(ScheduleType.DAY.getCode());
		record.setGroupId(form.getGroupId());
		record.setCompanyId(getCompanyId());
		record.setIsStat(EMPTY);

		if (isNotEmpty(form.getGroupCode())) {
			final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
			if (companyTree != null) {
				record.setGroupCode(form.getGroupCode());
				record.setGroupTreeName(companyTree.getGroupTreeName());
				record.setGroupLevel(companyTree.getGroupLevel());
			}
		}

		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo> dayScheduleInfos = findAll(TALK_SCHEDULE_INFO.TYPE.eq(ScheduleType.DAY.getCode()));

		for (LocalDate localDate : localDates) {
			record.setFromdate(Date.valueOf(localDate));
			record.setTodate(Date.valueOf(localDate));
			for (String senderKey: form.getSenderKeys()) {
				if (dayScheduleInfos.stream().noneMatch(e -> e.getSenderKey().equals(senderKey) && Date.valueOf(localDate).equals(e.getFromdate()))) {
					record.setSenderKey(senderKey);
					insertOnGeneratedKey(record);
				}
			}
		}
	}

	public void insertOnDayHolySchedule(HolyTalkScheduleInfoFormRequest form) {
		final List<PeriodDateFormRequest> periodDates = form.getPeriodDates();

		for (PeriodDateFormRequest periodDate : periodDates) {
			final DayTalkScheduleInfoFormRequest formRequest = new DayTalkScheduleInfoFormRequest();
			ReflectionUtils.copy(formRequest, form);
			formRequest.setFromDate(periodDate.getFromDate());
			formRequest.setToDate(periodDate.getToDate());

			insertOnDaySchedule(formRequest);
		}
	}

	public void deleteBySenderKey(String type, String senderKey) {
		super.delete(TALK_SCHEDULE_INFO.SENDER_KEY.eq(senderKey)
				.and(TALK_SCHEDULE_INFO.TYPE.eq(type)));
	}

	public Map<String, List<TalkScheduleInfoEntity>> getTalkServiceInfoLists(TalkServiceInfoSearchRequest search) {
		final SelectConditionStep<Record> query = dsl.select()
				.from(TALK_SCHEDULE_INFO)
				.innerJoin(TALK_SCHEDULE_GROUP)
				.on(TALK_SCHEDULE_INFO.GROUP_ID.eq(TALK_SCHEDULE_GROUP.PARENT))
				.where(compareCompanyId());

		if (isNotEmpty(search.getSenderKey()))
			query.and(TALK_SCHEDULE_INFO.SENDER_KEY.eq(search.getSenderKey()));
		if (Objects.nonNull(search.getGroupId()))
			query.and(TALK_SCHEDULE_INFO.GROUP_ID.eq(search.getGroupId()));
		if (isNotEmpty(search.getSearchDate())) {
			if (Objects.equals(ScheduleType.WEEK, search.getType()))
				query.and(TALK_SCHEDULE_INFO.WEEK.like("%" + search.getSearchDate()));
			else if (Objects.equals(ScheduleType.DAY, search.getType()))
				query.and(TALK_SCHEDULE_INFO.FROMDATE.greaterOrEqual(DSL.date(search.getSearchDate()))
						.and(TALK_SCHEDULE_INFO.TODATE.lessOrEqual(DSL.date(search.getSearchDate()))));
		}
		if (isNotEmpty(search.getGroupCode()))
			query.and(TALK_SCHEDULE_INFO.GROUP_CODE.eq(search.getGroupCode()));

		query.and(TALK_SCHEDULE_INFO.TYPE.eq(search.getType().getCode()));

		final Result<Record> result = query
				.orderBy(TALK_SCHEDULE_INFO.WEEK)
				.fetch();

		final List<TalkScheduleGroupEntity> talkScheduleGroupLists = talkScheduleGroupRepository.getTalkScheduleGroupLists();
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

		return result.stream().map(record -> {
			final TalkScheduleInfoEntity row = record.into(TalkScheduleInfoEntity.class);
			final TalkScheduleGroupEntity talkScheduleGroupEntity = record.into(TalkScheduleGroupEntity.class);

			talkScheduleGroupLists.stream()
					.filter(e -> e.getParent().equals(talkScheduleGroupEntity.getParent()))
					.findAny()
					.ifPresent(e -> talkScheduleGroupEntity.setScheduleGroupLists(e.getScheduleGroupLists()));

			if (isNotEmpty(row.getGroupCode()))
				row.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, row.getGroupCode())
						.stream()
						.map(group -> modelMapper.map(group, OrganizationSummaryResponse.class))
						.collect(Collectors.toList()));

			row.setScheduleGroup(talkScheduleGroupEntity);

			return row;
		}).collect(Collectors.groupingBy(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleInfo::getSenderKey));
	}
}
