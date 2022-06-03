package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkScheduleGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupListEntity;
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
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberGroup.WTALK_MEMBER_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMent.WTALK_MENT;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkScheduleGroup.WTALK_SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkScheduleGroupList.WTALK_SCHEDULE_GROUP_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotInfo.WEBCHAT_BOT_INFO;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class WtalkScheduleGroupRepository extends EicnBaseRepository<WtalkScheduleGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroup, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(WtalkScheduleGroupRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	WtalkScheduleGroupRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(WTALK_SCHEDULE_GROUP, WTALK_SCHEDULE_GROUP.PARENT, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroup.class);
		orderByFields.add(WTALK_SCHEDULE_GROUP.NAME.asc());

		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public List<WtalkScheduleGroupEntity> getTalkScheduleGroupLists() {
		final Map<Record, Result<Record>> recordResultMap = dsl.select()
				.from(WTALK_SCHEDULE_GROUP)
				.leftOuterJoin(WTALK_SCHEDULE_GROUP_LIST)
				.on(WTALK_SCHEDULE_GROUP.PARENT.eq(WTALK_SCHEDULE_GROUP_LIST.PARENT).and(WTALK_SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId())))
				.where(compareCompanyId())
				.orderBy(WTALK_SCHEDULE_GROUP.NAME.asc(), WTALK_SCHEDULE_GROUP_LIST.TOHOUR.asc())
				.fetch()
				.intoGroups(WTALK_SCHEDULE_GROUP.fields());

		final List<WtalkScheduleGroupEntity> talkScheduleGroupEntities = new ArrayList<>();
		final Map<Integer, String> talkMentStringMap = getTalkMentLists().stream().collect(Collectors.toMap(WtalkMent::getSeq, WtalkMent::getMentName));
		final Map<Integer, String> talkMemberGroupStringMap = getTalkMemberGroupLists().stream().collect(Collectors.toMap(WtalkMemberGroup::getGroupId, WtalkMemberGroup::getGroupName));
		final Map<Integer, String> webchatBotInfoMap = getWebchatBotinfoList().stream().collect(Collectors.toMap(WebchatBotInfo::getId, WebchatBotInfo::getName));

		recordResultMap.forEach((record, records) -> {
			final WtalkScheduleGroupEntity talkScheduleGroupEntity = record.into(WtalkScheduleGroupEntity.class);

			talkScheduleGroupEntity.setScheduleGroupLists(records.stream()
					.filter(r -> r.getValue(WTALK_SCHEDULE_GROUP_LIST.CHILD) != null)
					.map(r -> {
						final WtalkScheduleGroupListEntity into = r.into(WtalkScheduleGroupListEntity.class);
						if (isNotEmpty(into.getKindData())) {
							if (TalkScheduleKind.AUTO_MENT_REQUEST.getCode().equals(into.getKind()))
								into.setKindDataName(talkMentStringMap.getOrDefault(Integer.valueOf(into.getKindData()), EMPTY));
							else if (TalkScheduleKind.SERVICE_BY_GROUP_CONNECT.getCode().equals(into.getKind()))
								into.setKindDataName(talkMemberGroupStringMap.getOrDefault(Integer.valueOf(into.getKindData()), EMPTY));
							else if (TalkScheduleKind.CHAT_BOT_CONNECT.getCode().equals(into.getKind()))
								into.setKindDataName(webchatBotInfoMap.getOrDefault(Integer.valueOf(into.getKindData()), EMPTY));
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
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroup();
		ReflectionUtils.copy(record, form);

		record.setCompanyId(getCompanyId());
		record.setChannelType(form.getChannelType().getCode());

		super.insert(record);
	}

	private List<WtalkMent> getTalkMentLists() {
		return dsl.select()
				.from(WTALK_MENT)
				.where(WTALK_MENT.COMPANY_ID.eq(getCompanyId()))
				.orderBy(WTALK_MENT.MENT_NAME)
				.fetchInto(WtalkMent.class);
	}

	private List<WtalkMemberGroup> getTalkMemberGroupLists() {
		return dsl.select()
				.from(WTALK_MEMBER_GROUP)
				.where(WTALK_MEMBER_GROUP.COMPANY_ID.eq(getCompanyId()))
				.orderBy(WTALK_MEMBER_GROUP.GROUP_NAME)
				.fetchInto(WtalkMemberGroup.class);
	}

	private List<PersonList> getPersonLists() {
		return dsl.select()
				.from(PERSON_LIST)
				.where(PERSON_LIST.COMPANY_ID.eq(getCompanyId()))
				.orderBy(PERSON_LIST.ID_NAME)
				.fetchInto(PersonList.class);
	}

	private List<WebchatBotInfo> getWebchatBotinfoList(){
		return dsl.select()
				.from(WEBCHAT_BOT_INFO)
				.where(WEBCHAT_BOT_INFO.COMPANY_ID.eq(getCompanyId()))
				.orderBy(WEBCHAT_BOT_INFO.NAME)
				.fetchInto(WebchatBotInfo.class);
	}
}
