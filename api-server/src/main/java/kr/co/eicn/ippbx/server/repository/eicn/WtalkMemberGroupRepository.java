package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberList;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberGroup.WTALK_MEMBER_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberList.WTALK_MEMBER_LIST;
import static kr.co.eicn.ippbx.util.StringUtils.subStringBytes;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Getter
@Repository
public class WtalkMemberGroupRepository extends EicnBaseRepository<WtalkMemberGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberGroup, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(WtalkMemberGroupRepository.class);

	private final WtalkMemberListRepository talkMemberListRepository;
	private final WebSecureHistoryRepository webSecureHistoryRepository;

	WtalkMemberGroupRepository(WtalkMemberListRepository talkMemberListRepository, WebSecureHistoryRepository webSecureHistoryRepository) {
		super(WTALK_MEMBER_GROUP, WTALK_MEMBER_GROUP.GROUP_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberGroup.class);
		this.talkMemberListRepository = talkMemberListRepository;
		this.webSecureHistoryRepository = webSecureHistoryRepository;
	}

	public Record insertOnGeneratedKey(TalkMemberGroupFormRequest form) {
		// 상담톡 서비스별로 그룹을 1개씩만 설정 가능

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberGroup();
		record.setGroupName(form.getGroupName());
		record.setCompanyId(getCompanyId());
		record.setTalkStrategy(form.getDistributionPolicy().getCode());
		record.setInitMent(form.getInitMent());
		record.setAutoWarnMin(form.getAutoWarnMin());
		record.setAutoWarnMent(form.getAutoWarnMent());
		record.setAutoExpireMin(form.getAutoExpireMin());
		record.setAutoExpireMent(form.getAutoExpireMent());
		record.setUnassignCnt(form.getUnassignCnt());
		record.setUnassignMent(form.getUnassignMent());
		record.setMemberUnanswerMin(form.getMemberUnanswerMin());
		record.setMemberUnanswerMent(form.getMemberUnanswerMent());
		record.setDistLastTime(Timestamp.valueOf("2020-05-01 00:00:00"));
		record.setDistLastUserid("");

		final Record r = super.insertOnGeneratedKey(record);

		final WtalkMemberList talkMemberRecord = new WtalkMemberList();
		talkMemberRecord.setCompanyId(getCompanyId());
		talkMemberRecord.setGroupId(r.getValue(WTALK_MEMBER_GROUP.GROUP_ID));
		talkMemberRecord.setStatus(EMPTY);

		int sequence = 0;
		for (String personId : form.getPersonIds()) {
			talkMemberRecord.setUserid(personId);
			talkMemberRecord.setDistSequence(sequence++);
			talkMemberRecord.setDistTodayCount(0);
			talkMemberRecord.setDistRemainCount(0);
			talkMemberRecord.setDistLastTime(Timestamp.valueOf("2020-05-01 00:00:00"));
			talkMemberRecord.setIsDistEnable("Y");
			talkMemberListRepository.insert(talkMemberRecord);
		}

		webSecureHistoryRepository.insert(WebSecureActionType.TALK, WebSecureActionSubType.ADD, subStringBytes(form.getGroupName() + "," + String.join("|", form.getPersonIds()), 400));

		return r;
	}

	public void updateByKey(TalkMemberGroupFormRequest form, Integer groupId) {
		findOneIfNullThrow(groupId);


		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberGroup();
		ReflectionUtils.copy(record, findOne(groupId));

		record.setGroupName(form.getGroupName());
		record.setTalkStrategy(form.getDistributionPolicy().getCode());
		record.setInitMent(form.getInitMent());
		record.setAutoWarnMin(form.getAutoWarnMin());
		record.setAutoWarnMent(form.getAutoWarnMent());
		record.setAutoExpireMin(form.getAutoExpireMin());
		record.setAutoExpireMent(form.getAutoExpireMent());
		record.setUnassignCnt(form.getUnassignCnt());
		record.setUnassignMent(form.getUnassignMent());
		record.setMemberUnanswerMin(form.getMemberUnanswerMin());
		record.setMemberUnanswerMent(form.getMemberUnanswerMent());

		super.updateByKey(record, groupId);

		// 기존 그룹 사용자
		final Map<String, WtalkMemberList> persons = talkMemberListRepository.findAll(WTALK_MEMBER_LIST.GROUP_ID.eq(groupId)).stream().collect(Collectors.toMap(WtalkMemberList::getUserid, e -> e));
		talkMemberListRepository.delete(WTALK_MEMBER_LIST.GROUP_ID.eq(groupId));

		final WtalkMemberList talkMemberRecord = new WtalkMemberList();
		talkMemberRecord.setCompanyId(getCompanyId());
		talkMemberRecord.setGroupId(groupId);
		talkMemberRecord.setStatus(EMPTY);

		int sequence = 0;
		for (String personId : form.getPersonIds()) {
			talkMemberRecord.setUserid(personId);
			talkMemberRecord.setDistSequence(sequence++);
			talkMemberRecord.setDistTodayCount(0);
			talkMemberRecord.setDistRemainCount(0);
			talkMemberRecord.setDistLastTime(Timestamp.valueOf("2020-05-01 00:00:00"));
			talkMemberRecord.setIsDistEnable("Y");

			if (Objects.nonNull(persons.get(personId)))
				talkMemberRecord.setStatus(persons.get(personId).getStatus());

			talkMemberListRepository.insert(talkMemberRecord);
		}

		webSecureHistoryRepository.insert(WebSecureActionType.TALK, WebSecureActionSubType.MOD
				, subStringBytes(form.getGroupName() + "(" + groupId + ")" + String.join("|", form.getPersonIds()), 400));
	}

	public void deleteWithPersons(Integer groupId) {
		deleteOnIfNullThrow(groupId);

		talkMemberListRepository.delete(WTALK_MEMBER_LIST.GROUP_ID.eq(groupId));

		webSecureHistoryRepository.insert(WebSecureActionType.TALK , WebSecureActionSubType.DEL, String.valueOf(groupId));
	}
}
