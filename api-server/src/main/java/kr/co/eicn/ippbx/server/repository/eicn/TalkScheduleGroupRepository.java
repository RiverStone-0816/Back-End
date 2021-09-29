package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMent;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleGroupListEntity;
import kr.co.eicn.ippbx.model.enums.TalkScheduleKind;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberGroup.TALK_MEMBER_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMent.TALK_MENT;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleGroup.TALK_SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleGroupList.TALK_SCHEDULE_GROUP_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class TalkScheduleGroupRepository extends EicnBaseRepository<TalkScheduleGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroup, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkScheduleGroupRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	TalkScheduleGroupRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(TALK_SCHEDULE_GROUP, TALK_SCHEDULE_GROUP.PARENT, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroup.class);
		orderByFields.add(TALK_SCHEDULE_GROUP.NAME.asc());

		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public List<TalkScheduleGroupEntity> getTalkScheduleGroupLists() {
		final Map<Record, Result<Record>> recordResultMap = dsl.select()
				.from(TALK_SCHEDULE_GROUP)
				.leftOuterJoin(TALK_SCHEDULE_GROUP_LIST)
				.on(TALK_SCHEDULE_GROUP.PARENT.eq(TALK_SCHEDULE_GROUP_LIST.PARENT).and(TALK_SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId())))
				.where(compareCompanyId())
				.orderBy(TALK_SCHEDULE_GROUP.NAME.asc(), TALK_SCHEDULE_GROUP_LIST.TOHOUR.asc())
				.fetch()
				.intoGroups(TALK_SCHEDULE_GROUP.fields());

		final List<TalkScheduleGroupEntity> talkScheduleGroupEntities = new ArrayList<>();
		final Map<Integer, String> talkMentStringMap = getTalkMentLists().stream().collect(Collectors.toMap(TalkMent::getSeq, TalkMent::getMentName));
		final Map<Integer, String> talkMemberGroupStringMap = getTalkMemberGroupLists().stream().collect(Collectors.toMap(TalkMemberGroup::getGroupId, TalkMemberGroup::getGroupName));
		final Map<String, String> personStringMap = getPersonLists().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));

		recordResultMap.forEach((record, records) -> {
			final TalkScheduleGroupEntity talkScheduleGroupEntity = record.into(TalkScheduleGroupEntity.class);

			talkScheduleGroupEntity.setScheduleGroupLists(records.stream()
					.filter(r -> r.getValue(TALK_SCHEDULE_GROUP_LIST.CHILD) != null)
					.map(r -> {
						final TalkScheduleGroupListEntity into = r.into(TalkScheduleGroupListEntity.class);
						if (isNotEmpty(into.getKindData())) {
							if (TalkScheduleKind.AUTO_MENT_REQUEST.getCode().equals(into.getKind()))
								into.setKindDataName(talkMentStringMap.getOrDefault(Integer.valueOf(into.getKindData()), EMPTY));
							else if (TalkScheduleKind.SERVICE_BY_GROUP_CONNECT.getCode().equals(into.getKind()))
								into.setKindDataName(talkMemberGroupStringMap.getOrDefault(Integer.valueOf(into.getKindData()), EMPTY));
							else if (TalkScheduleKind.PERSON_CONSULTATION_CONNECT.getCode().equals(into.getKindData()))
								into.setKindDataName(personStringMap.getOrDefault(into.getKind(), EMPTY));
						}

						return into;
					})
					.collect(Collectors.toList())
			);

			talkScheduleGroupEntities.add(talkScheduleGroupEntity);
		});

		return talkScheduleGroupEntities;
	}

	public void insert(TalkScheduleGroupFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroup();
		ReflectionUtils.copy(record, form);

		record.setCompanyId(getCompanyId());

		super.insert(record);
		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
					super.insert(pbxDsl, record);
				});
	}

	private List<TalkMent> getTalkMentLists() {
		return dsl.select()
				.from(TALK_MENT)
				.where(TALK_MENT.COMPANY_ID.eq(getCompanyId()))
				.orderBy(TALK_MENT.MENT_NAME)
				.fetchInto(TalkMent.class);
	}

	private List<TalkMemberGroup> getTalkMemberGroupLists() {
		return dsl.select()
				.from(TALK_MEMBER_GROUP)
				.where(TALK_MEMBER_GROUP.COMPANY_ID.eq(getCompanyId()))
				.orderBy(TALK_MEMBER_GROUP.GROUP_NAME)
				.fetchInto(TalkMemberGroup.class);
	}

	private List<PersonList> getPersonLists() {
		return dsl.select()
				.from(PERSON_LIST)
				.where(PERSON_LIST.COMPANY_ID.eq(getCompanyId()))
				.orderBy(PERSON_LIST.ID_NAME)
				.fetchInto(PersonList.class);
	}
}
