package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleGroupList;
import kr.co.eicn.ippbx.model.enums.ScheduleListKind;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.enums.TalkScheduleKind;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkScheduleGroupList.TALK_SCHEDULE_GROUP_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Getter
@Repository
public class TalkScheduleGroupListRepository extends EicnBaseRepository<TalkScheduleGroupList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroupList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkScheduleGroupListRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	TalkScheduleGroupListRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(TALK_SCHEDULE_GROUP_LIST, TALK_SCHEDULE_GROUP_LIST.CHILD, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroupList.class);
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public void insert(TalkScheduleGroupListFormRequest form) {
		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroupList> duplicatedTime =
				findAll(TALK_SCHEDULE_GROUP_LIST.FROMHOUR.greaterThan(form.getFromhour()).and(TALK_SCHEDULE_GROUP_LIST.FROMHOUR.lessThan(form.getTohour()))
								.or(TALK_SCHEDULE_GROUP_LIST.TOHOUR.greaterThan(form.getFromhour()).and(TALK_SCHEDULE_GROUP_LIST.TOHOUR.lessThan(form.getTohour())))
								.and(TALK_SCHEDULE_GROUP_LIST.PARENT.eq(form.getParent()))
				);
		if (duplicatedTime.size() > 0)
			throw new DuplicateKeyException("이미 사용중인 시간이 있습니다. 삭제후 등록해 주세요.");

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroupList record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroupList();
		ReflectionUtils.copy(record, form);

		if(form.getKind().equals(TalkScheduleKind.SERVICE_BY_GROUP_CONNECT.getCode()))
			record.setKindData(form.getTalkGroup());

		if(form.getChannelType().equals(TalkChannelType.EICN.getCode()) && form.getKind().equals(TalkScheduleKind.CHAT_BOT_CONNECT.getCode()))
			record.setKindData(form.getChatBot());

		record.setChildName(EMPTY);
		record.setStatYn(defaultString(form.getStatYn(), "Y"));
		record.setWorktimeYn(defaultString(form.getWorktimeYn(), "Y"));
		record.setCompanyId(getCompanyId());

		super.insert(record);
	}

	public void updateByKey(TalkScheduleGroupListFormRequest form, Integer key) {
		if (ScheduleListKind.AUTO_MENT_REQUEST.getCode().equals(form.getKind())) {
			// FIXME: 0 으로 채우지 않아도 상관없는지 확인 필요
			// form.setFirstMentId(0);
			// form.setLimitNum(0);
			// form.setLimitMentId(0);
		}
		if (TalkScheduleKind.CHAT_BOT_CONNECT.getCode().equals(form.getKind()))
			form.setKindData(form.getChatBot());

		super.updateByKey(form, key);
	}
}
