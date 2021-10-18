package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberList;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberGroup.TALK_MEMBER_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberList.TALK_MEMBER_LIST;
import static kr.co.eicn.ippbx.util.StringUtils.subStringBytes;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Getter
@Repository
public class TalkMemberGroupRepository extends EicnBaseRepository<TalkMemberGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkMemberGroupRepository.class);

	private final TalkMemberListRepository talkMemberListRepository;
	private final WebSecureHistoryRepository webSecureHistoryRepository;

	TalkMemberGroupRepository(TalkMemberListRepository talkMemberListRepository, WebSecureHistoryRepository webSecureHistoryRepository) {
		super(TALK_MEMBER_GROUP, TALK_MEMBER_GROUP.GROUP_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup.class);
		this.talkMemberListRepository = talkMemberListRepository;
		this.webSecureHistoryRepository = webSecureHistoryRepository;
	}

	public Record insertOnGeneratedKey(TalkMemberGroupFormRequest form) {
		// 상담톡 서비스별로 그룹을 1개씩만 설정 가능

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup();
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

		final TalkMemberList talkMemberRecord = new TalkMemberList();
		talkMemberRecord.setCompanyId(getCompanyId());
		talkMemberRecord.setGroupId(r.getValue(TALK_MEMBER_GROUP.GROUP_ID));
		talkMemberRecord.setStatus(EMPTY);

		int sequence = 0;
		for (String personId : form.getPersonIds()) {
			talkMemberRecord.setUserid(personId);
			talkMemberRecord.setDistSequence(sequence++);
			talkMemberRecord.setDistTodayCount(0);
			talkMemberRecord.setDistRemainCount(0);
			talkMemberRecord.setDistLastTime(Timestamp.valueOf("2020-05-01 00:00:00"));
			talkMemberRecord.setIsDistEnable("");
			talkMemberListRepository.insert(talkMemberRecord);
		}

		webSecureHistoryRepository.insert(WebSecureActionType.TALK, WebSecureActionSubType.ADD, subStringBytes(form.getGroupName() + "," + String.join("|", form.getPersonIds()), 400));

		return r;
	}

	public void updateByKey(TalkMemberGroupFormRequest form, Integer groupId) {
		findOneIfNullThrow(groupId);


		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberGroup();
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
		final Map<String, TalkMemberList> persons = talkMemberListRepository.findAll(TALK_MEMBER_LIST.GROUP_ID.eq(groupId)).stream().collect(Collectors.toMap(TalkMemberList::getUserid, e -> e));
		talkMemberListRepository.delete(TALK_MEMBER_LIST.GROUP_ID.eq(groupId));

		final TalkMemberList talkMemberRecord = new TalkMemberList();
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

			if (Objects.nonNull(persons.get(personId)))
				talkMemberRecord.setStatus(persons.get(personId).getStatus());

			talkMemberListRepository.insert(talkMemberRecord);
		}

		webSecureHistoryRepository.insert(WebSecureActionType.TALK, WebSecureActionSubType.MOD
				, subStringBytes(form.getGroupName() + "(" + groupId + ")" + String.join("|", form.getPersonIds()), 400));
	}

	public void deleteWithPersons(Integer groupId) {
		deleteOnIfNullThrow(groupId);

		talkMemberListRepository.delete(TALK_MEMBER_LIST.GROUP_ID.eq(groupId));

		webSecureHistoryRepository.insert(WebSecureActionType.TALK , WebSecureActionSubType.DEL, String.valueOf(groupId));
	}
}
