package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkScheduleGroupList;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkScheduleGroupList.WTALK_SCHEDULE_GROUP_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Getter
@Repository
public class WtalkScheduleGroupListRepository extends EicnBaseRepository<WtalkScheduleGroupList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroupList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(WtalkScheduleGroupListRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	WtalkScheduleGroupListRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(WTALK_SCHEDULE_GROUP_LIST, WTALK_SCHEDULE_GROUP_LIST.CHILD, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroupList.class);
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public void insert(TalkScheduleGroupListFormRequest form) {
		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroupList> duplicatedTime =
				findAll(WTALK_SCHEDULE_GROUP_LIST.FROMHOUR.greaterThan(form.getFromhour()).and(WTALK_SCHEDULE_GROUP_LIST.FROMHOUR.lessThan(form.getTohour()))
								.or(WTALK_SCHEDULE_GROUP_LIST.TOHOUR.greaterThan(form.getFromhour()).and(WTALK_SCHEDULE_GROUP_LIST.TOHOUR.lessThan(form.getTohour())))
								.and(WTALK_SCHEDULE_GROUP_LIST.PARENT.eq(form.getParent()))
				);
		if (duplicatedTime.size() > 0)
			throw new DuplicateKeyException("이미 사용중인 시간이 있습니다. 삭제후 등록해 주세요.");

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroupList record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroupList();
		ReflectionUtils.copy(record, form);

		if(TalkScheduleKind.SERVICE_BY_GROUP_CONNECT.getCode().equals(form.getKind()))
			record.setKindData(form.getTalkGroup());
		else if(TalkScheduleKind.CHAT_BOT_CONNECT.getCode().equals(form.getKind()))
			record.setKindData(form.getChatBot());

		record.setChildName(EMPTY);
		record.setStatYn(defaultString(form.getStatYn(), "Y"));
		record.setWorktimeYn(defaultString(form.getWorktimeYn(), "Y"));
		record.setCompanyId(getCompanyId());

		super.insert(record);
	}

	public void updateByKey(TalkScheduleGroupListFormRequest form, Integer key) {
		if(TalkScheduleKind.SERVICE_BY_GROUP_CONNECT.getCode().equals(form.getKind()))
			form.setKindData(form.getTalkGroup());
		else if (TalkScheduleKind.CHAT_BOT_CONNECT.getCode().equals(form.getKind()))
			form.setKindData(form.getChatBot());

		super.updateByKey(form, key);
	}
}
