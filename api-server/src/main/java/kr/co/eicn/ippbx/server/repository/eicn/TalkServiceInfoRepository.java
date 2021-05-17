package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.TalkServiceInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.TalkScheduleGroupEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.TalkScheduleInfoEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.TalkServiceInfoEntity;
import kr.co.eicn.ippbx.server.model.enums.ScheduleType;
import kr.co.eicn.ippbx.server.model.form.TalkServiceInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.TalkScheduleGroup.TALK_SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.TalkScheduleInfo.TALK_SCHEDULE_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.TalkServiceInfo.TALK_SERVICE_INFO;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class TalkServiceInfoRepository extends EicnBaseRepository<TalkServiceInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkServiceInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkServiceInfoRepository.class);
	private final TalkScheduleGroupRepository talkScheduleGroupRepository;
	private final OrganizationService organizationService;

	TalkServiceInfoRepository(TalkScheduleGroupRepository talkScheduleGroupRepository, OrganizationService organizationService) {
		super(TALK_SERVICE_INFO, TALK_SERVICE_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkServiceInfo.class);
		this.talkScheduleGroupRepository = talkScheduleGroupRepository;
		this.organizationService = organizationService;
	}

	public Record insertOnGeneratedKey(TalkServiceInfoFormRequest form) {
		final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkServiceInfo record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkServiceInfo();

		record.setCompanyId(getCompanyId());
		record.setSenderKey(form.getSenderKey());
		record.setKakaoServiceId(form.getKakaoServiceId());
		record.setKakaoServiceName(form.getKakaoServiceName());
		record.setIsChattEnable(form.getIsChattEnable());

		return super.insertOnGeneratedKey(record);
	}

	public List<TalkServiceInfoEntity> getTalkServiceInfoLists(TalkServiceInfoSearchRequest search) {
		final SelectConditionStep<Record> query = dsl.select()
				.from(TALK_SERVICE_INFO)
				.leftOuterJoin(TALK_SCHEDULE_INFO)
				.on(TALK_SERVICE_INFO.SENDER_KEY.eq(TALK_SCHEDULE_INFO.SENDER_KEY))
				.innerJoin(TALK_SCHEDULE_GROUP)
				.on(TALK_SCHEDULE_INFO.GROUP_ID.eq(TALK_SCHEDULE_GROUP.PARENT))
				.where(compareCompanyId());

		if (isNotEmpty(search.getSenderKey()))
			query.and(TALK_SERVICE_INFO.SENDER_KEY.eq(search.getSenderKey()));
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

		final Map<Record, Result<Record>> recordResultMap = query
				.orderBy(TALK_SCHEDULE_INFO.WEEK)
				.fetch()
				.intoGroups(TALK_SERVICE_INFO.fields());

		final List<TalkServiceInfoEntity> talkServiceInfoEntities = new ArrayList<>();
		final List<TalkScheduleGroupEntity> talkScheduleGroupLists = talkScheduleGroupRepository.getTalkScheduleGroupLists();
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

		recordResultMap.forEach((record, records) -> {
			final TalkServiceInfoEntity talkServiceInfoEntity = record.into(TalkServiceInfoEntity.class);

			talkServiceInfoEntity.setScheduleInfos(records.stream()
				.filter(r -> r.getValue(TALK_SCHEDULE_INFO.SEQ) != null)
				.map(r -> {
					final TalkScheduleInfoEntity into = r.into(TalkScheduleInfoEntity.class);
					final TalkScheduleGroupEntity talkScheduleGroupEntity = r.into(TalkScheduleGroupEntity.class);

					talkScheduleGroupLists.stream()
							.filter(e -> e.getParent().equals(talkScheduleGroupEntity.getParent()))
							.findAny()
							.ifPresent(e -> talkScheduleGroupEntity.setScheduleGroupLists(e.getScheduleGroupLists()));

					if (StringUtils.isNotEmpty(into.getGroupCode()))
						into.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, into.getGroupCode())
								.stream()
								.map(group -> modelMapper.map(group, OrganizationSummaryResponse.class))
								.collect(Collectors.toList()));

					into.setScheduleGroup(talkScheduleGroupEntity);

					return into;
				})
				.collect(Collectors.toList())
			);

			talkServiceInfoEntities.add(talkServiceInfoEntity);
		});

		return talkServiceInfoEntities;
	}
}
