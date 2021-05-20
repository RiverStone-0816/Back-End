package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailMemberGroup;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.EmailReceiveGroupFormRequest;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.*;
import static kr.co.eicn.ippbx.util.StringUtils.subStringBytes;
import static org.jooq.tools.StringUtils.EMPTY;

@Getter
@Repository
public class EmailMemberGroupRepository extends EicnBaseRepository<EmailMemberGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(EmailMemberGroupRepository.class);

    private final EmailMngRepository emailMngRepository;
    private final EmailMemberListRepository emailMemberListRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;

    EmailMemberGroupRepository(EmailMngRepository emailMngRepository, EmailMemberListRepository emailMemberListRepository, WebSecureHistoryRepository webSecureHistoryRepository) {
        super(EMAIL_MEMBER_GROUP, EMAIL_MEMBER_GROUP.GROUP_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup.class);
        this.emailMngRepository = emailMngRepository;
        this.emailMemberListRepository = emailMemberListRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
    }

    public Record insertOnGeneratedKey(EmailReceiveGroupFormRequest form) {
        final EmailServiceInfo emailServiceInfo = emailMngRepository.findOne(EMAIL_SERVICE_INFO.SEQ.eq(form.getEmailId()));
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup emailMemberGroup = findOne(EMAIL_MEMBER_GROUP.EMAIL_ID.eq(form.getEmailId()));
        if (emailMemberGroup != null) {
            throw new IllegalArgumentException(emailServiceInfo.getServiceName() + "은(는) 이미 등록된 그룹명입니다.");
        }

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup memberGroupRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup();
        memberGroupRecord.setGroupName(form.getGroupName());
        memberGroupRecord.setEmailId(form.getEmailId());
        memberGroupRecord.setCompanyId(getCompanyId());

        final Record record = super.insertOnGeneratedKey(memberGroupRecord);

        final EmailMemberList memberListRecord = new EmailMemberList();
        memberListRecord.setGroupId(record.getValue(EMAIL_MEMBER_GROUP.GROUP_ID));
        memberListRecord.setStatus(EMPTY);
        memberListRecord.setCompanyId(getCompanyId());
        memberListRecord.setEmailId(form.getEmailId());

        for(String memberId : form.getEmailMemberLists()) {
            memberListRecord.setUserid(memberId);
            emailMemberListRepository.insert(memberListRecord);
        }

        webSecureHistoryRepository.insert(WebSecureActionType.EMAIL, WebSecureActionSubType.ADD, subStringBytes(form.getGroupName() + "," + String.join("|", form.getEmailMemberLists()), 400));

        return record;
    }

    public void emailGroupUpdate(EmailReceiveGroupFormRequest form, Integer groupId) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup record = findOneIfNullThrow(groupId);

        final EmailServiceInfo emailServiceInfo = emailMngRepository.findOne(EMAIL_SERVICE_INFO.SEQ.eq(form.getEmailId()));
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberGroup emailMemberGroup =
                findOne(EMAIL_MEMBER_GROUP.GROUP_ID.notEqual(groupId).and(EMAIL_MEMBER_GROUP.EMAIL_ID.eq(form.getEmailId())));
        if (emailMemberGroup != null) {
            throw new IllegalArgumentException(emailServiceInfo.getServiceName() + "은(는) 이미 등록된 그룹명입니다.");
        }

        record.setGroupName(form.getGroupName());
        record.setEmailId(form.getEmailId());

        super.updateByKey(record, groupId);

        final Map<String, EmailMemberList> emailMemberLists = emailMemberListRepository.findAll(EMAIL_MEMBER_LIST.GROUP_ID.eq(groupId)).stream().collect(Collectors.toMap(EmailMemberList::getUserid, e -> e));
        emailMemberListRepository.delete(EMAIL_MEMBER_LIST.GROUP_ID.eq(groupId));

        final EmailMemberList memberListRecord = new EmailMemberList();
        memberListRecord.setCompanyId(getCompanyId());
        memberListRecord.setGroupId(groupId);
        memberListRecord.setEmailId(form.getEmailId());

        for (String memberId : form.getEmailMemberLists()) {
            memberListRecord.setUserid(memberId);
            if (Objects.nonNull(emailMemberLists.get(memberId)))
                memberListRecord.setStatus(emailMemberLists.get(memberId).getStatus());

            emailMemberListRepository.insert(memberListRecord);
        }

        webSecureHistoryRepository.insert(WebSecureActionType.EMAIL, WebSecureActionSubType.MOD
                , subStringBytes(form.getGroupName() + "(" + groupId + ")" + String.join("|", form.getEmailMemberLists()), 400));
    }

    public void emailGroupDelete(Integer groupId) {
        deleteOnIfNullThrow(groupId);

        emailMemberListRepository.delete(EMAIL_MEMBER_LIST.GROUP_ID.eq(groupId));

        webSecureHistoryRepository.insert(WebSecureActionType.EMAIL, WebSecureActionSubType.DEL, String.valueOf(groupId));
    }
}
