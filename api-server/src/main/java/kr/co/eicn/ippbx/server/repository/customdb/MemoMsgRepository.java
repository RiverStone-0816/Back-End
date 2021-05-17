package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.CommonMemoMsg;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.records.MemoMsgRecord;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.model.entity.customdb.MemoMsgEntity;
import kr.co.eicn.ippbx.server.model.enums.Bool;
import kr.co.eicn.ippbx.server.model.enums.ChattingJoinStatus;
import kr.co.eicn.ippbx.server.model.enums.ChattingSendReceive;
import kr.co.eicn.ippbx.server.model.form.MemoMsgFormRequest;
import kr.co.eicn.ippbx.server.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.server.model.search.MemoMsgSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MemoMsgRepository extends CustomDBBaseRepository<CommonMemoMsg, MemoMsgEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(MemoMsgRepository.class);

    private final CommonMemoMsg TABLE;
    @Autowired
    private PersonListRepository personListRepository;

    public MemoMsgRepository(String companyId) {
        super(new CommonMemoMsg(companyId), new CommonMemoMsg(companyId).SEQ, MemoMsgEntity.class);
        this.TABLE = new CommonMemoMsg(companyId);

        addOrderingField(TABLE.INSERT_TIME.desc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        query.groupBy(TABLE.MESSAGE_ID);

        return query.where();
    }

    @Override
    protected RecordMapper<Record, MemoMsgEntity> getMapper() {
        return record -> record.into(TABLE).into(MemoMsgEntity.class);
    }

    public String insertMemoMessage(Integer seq, MemoMsgFormRequest form) {
        final String messageId = g.getUser().getId() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonMemoMsg memoMsg = new kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonMemoMsg();
        memoMsg.setMessageId(messageId);
        memoMsg.setSendUserid(g.getUser().getId());
        memoMsg.setInsertTime(new Timestamp(System.currentTimeMillis()));
        memoMsg.setContent(form.getContent());
        memoMsg.setReadYn(Bool.N.name());
        memoMsg.setSendReceive(ChattingSendReceive.SEND.getCode());
        memoMsg.setReceiveUserid(g.getUser().getId());
        super.insert(memoMsg);
        memoMsg.setSendReceive(ChattingSendReceive.RECEIVE.getCode());
        if (seq != null) {
            final MemoMsgEntity entity = findOneIfNullThrow(seq);
            memoMsg.setReceiveUserid(entity.getSendUserid());
            if (!entity.getSendUserid().equals(g.getUser().getId()))
                super.insert(memoMsg);
        } else {
            if (Objects.nonNull(form.getReceiveUserIds())) {
                for (String userId : form.getReceiveUserIds()) {
                    if (Boolean.TRUE.equals(form.getIsWriteToMe()) && userId.equals(g.getUser().getId()))
                        memoMsg.setSendReceive(ChattingSendReceive.SELF.getCode());
                    memoMsg.setReceiveUserid(userId);
                    super.insert(memoMsg);
                }
            }
        }
        return messageId;
    }

    public void updateReadYn(Integer seq) {
        final MemoMsgEntity entity = findOne(seq);
        final Condition condition = TABLE.SEQ.eq(seq).or(TABLE.MESSAGE_ID.eq(entity.getMessageId()));
        dsl.update(TABLE)
                .set(TABLE.READ_YN, Bool.Y.name())
                .where(entity.getSendUserid().equals(entity.getReceiveUserid()) ? condition : TABLE.SEQ.eq(seq))
                .execute();
    }

    public void deleteMemoMessages(List<Integer> memoSequences, List<String> messageIds) {
        if (Objects.nonNull(memoSequences))
            for (Integer seq : memoSequences) {
                final MemoMsgEntity entity = findOneIfNullThrow(seq);
                delete(seq);

                if (findAll(TABLE.MESSAGE_ID.eq(entity.getMessageId())).size() <= 2)
                    delete(TABLE.MESSAGE_ID.eq(entity.getMessageId()));
            }
        else
            for (String messageId : messageIds) {
                delete(TABLE.MESSAGE_ID.eq(messageId).and(TABLE.RECEIVE_USERID.eq(g.getUser().getId())));
            }
    }

    public Integer getUnreadMessageCount() {
        return findAll(TABLE.READ_YN.eq(ChattingJoinStatus.INACTIVE.getCode())
                .and(TABLE.SEND_RECEIVE.notEqual(ChattingSendReceive.SEND.getCode()))
                .and(TABLE.RECEIVE_USERID.eq(g.getUser().getId()))).size();
    }

    public Pagination<MemoMsgEntity> getSendMemoList(MemoMsgSearchRequest search) {
        return pagination(search, getMemoConditions(search, ChattingSendReceive.SEND.getCode()));
    }

    public Pagination<MemoMsgEntity> getReceiveMemoList(MemoMsgSearchRequest search) {
        return pagination(search, getMemoConditions(search, ChattingSendReceive.RECEIVE.getCode()));
    }

    public Map<String, List<MemoMsgEntity>> getReceiveUsers() {
        return dsl.selectFrom(TABLE)
                .where(TABLE.SEND_RECEIVE.notEqual(ChattingSendReceive.SEND.getCode()))
                .fetchGroups(TABLE.MESSAGE_ID, MemoMsgEntity.class);
    }

    public List<MemoMsgEntity> getReceiveUserByMessageId(String messageId) {
        return dsl.selectFrom(TABLE)
                .where(TABLE.MESSAGE_ID.eq(messageId))
                .and(TABLE.SEND_RECEIVE.notEqual(ChattingSendReceive.SEND.getCode()))
                .fetchInto(MemoMsgEntity.class);
    }

    public void userNameCondition(List<Condition> conditions, String userName, String type) {
        final ChattingMemberSearchRequest search = new ChattingMemberSearchRequest();
        search.setUserName(userName);
        List<String> userIds = personListRepository.findAllByChatting(search).stream().map(PersonList::getId).collect(Collectors.toList());
        if (userIds.size() > 0) {
            Condition condition = type.equals(ChattingSendReceive.RECEIVE.getCode()) ? TABLE.SEND_USERID.in(userIds) : TABLE.RECEIVE_USERID.in(userIds);
            List<String> messageIds = dsl.selectDistinct(TABLE.MESSAGE_ID).from(TABLE).where(condition).fetchInto(String.class);
            conditions.add(TABLE.MESSAGE_ID.in(messageIds));
        } else {
            if (Objects.equals(type, ChattingSendReceive.RECEIVE.getCode()))
                conditions.add(TABLE.SEND_USERID.like("%" + userName + "%"));
            else
                conditions.add(TABLE.RECEIVE_USERID.like("%" + userName + "%"));
        }
    }

    public List<Condition> getMemoConditions(MemoMsgSearchRequest search, String type) {
        List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.INSERT_TIME.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.INSERT_TIME.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (StringUtils.isNotEmpty(search.getContent()))
            conditions.add(TABLE.CONTENT.like("%" + search.getContent() + "%"));

        if (Objects.equals(type, ChattingSendReceive.RECEIVE.getCode())) {
            if (search.getDisplayUnreadMemo())
                conditions.add(TABLE.READ_YN.eq(Bool.Y.name()));
            if (Objects.nonNull(search.getSendUser()))
                userNameCondition(conditions, search.getSendUser(), ChattingSendReceive.RECEIVE.getCode());
            conditions.add(TABLE.SEND_RECEIVE.notEqual(ChattingSendReceive.SEND.getCode()).and(TABLE.RECEIVE_USERID.eq(g.getUser().getId())));
        } else {
            if (Objects.nonNull(search.getReceiveUser()))
                userNameCondition(conditions, search.getReceiveUser(), ChattingSendReceive.SEND.getCode());
            conditions.add(TABLE.SEND_RECEIVE.notEqual(ChattingSendReceive.RECEIVE.getCode()).and(TABLE.SEND_USERID.eq(g.getUser().getId())));
        }

        return conditions;
    }
}
