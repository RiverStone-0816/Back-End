package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.CommonChattMsg;
import kr.co.eicn.ippbx.server.model.entity.customdb.ChattMsgEntity;
import kr.co.eicn.ippbx.server.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.server.model.entity.customdb.ChattRoomMemberEntity;
import kr.co.eicn.ippbx.server.model.enums.MessageType;
import kr.co.eicn.ippbx.server.model.search.ChattingSearchRequest;
import kr.co.eicn.ippbx.server.service.ChattMemberService;
import kr.co.eicn.ippbx.server.service.ChattRoomService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Getter
public class ChattMsgRepository extends CustomDBBaseRepository<CommonChattMsg, ChattMsgEntity, Integer>{
    protected final Logger logger = LoggerFactory.getLogger(ChattMsgRepository.class);

    private final CommonChattMsg TABLE;
    @Autowired
    private ChattRoomService chattRoomService;
    @Autowired
    private ChattMemberService chattMemberService;

    public ChattMsgRepository(String companyId) {
        super(new CommonChattMsg(companyId), new CommonChattMsg(companyId).SEQ, ChattMsgEntity.class);
        this.TABLE = new CommonChattMsg(companyId);

        addOrderingField(TABLE.INSERT_TIME.asc());
    }

    public Integer findOneSeqByMessageId(String messageId) {
        return dsl.select(TABLE.SEQ)
                .from(TABLE)
                .where(TABLE.MESSAGE_ID.eq(messageId))
                .fetchOneInto(Integer.class);
    }

    public ChattMsgEntity findOneOrderByDesc() {
        return dsl.selectFrom(TABLE)
                .where(DSL.noCondition())
                .orderBy(TABLE.INSERT_TIME.desc())
                .limit(1)
                .fetchOneInto(ChattMsgEntity.class);
    }

    public List<ChattMsgEntity> findAllMsgByRoomId(ChattRoomEntity roomEntity, ChattingSearchRequest search, Map<String, ChattRoomMemberEntity> memberEntityMap) {
        final Integer startSeq = findOneSeqByMessageId(search.getStartMessageId());
        final Integer endSeq = findOneSeqByMessageId(search.getEndMessageId());
        final Condition condition = TABLE.ROOM_ID.eq(roomEntity.getRoomId()).and(TABLE.INSERT_TIME.ge(memberEntityMap.get(g.getUser().getId()).getInviteTime()));

        int limit = search.getLimit();
        if (Objects.nonNull(search.getStartMessageId()) && Objects.nonNull(search.getEndMessageId()))
            return findAll(dsl, condition, Collections.singletonList(TABLE.INSERT_TIME.desc()));

        return dsl.select(TABLE.fields())
                .from(TABLE)
                .where(getMessageConditions(condition, search, startSeq, endSeq))
                .orderBy(TABLE.INSERT_TIME.desc(), TABLE.SEQ.asc())
                .limit(limit)
                .fetchInto(ChattMsgEntity.class);
    }

    public List<Condition> getMessageConditions(Condition condition, ChattingSearchRequest search, Integer startSeq, Integer endSeq) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(condition);
        if (StringUtils.isNotEmpty(search.getMessage()))
            conditions.add(TABLE.TYPE.eq(MessageType.TEXT.getCode()).and(TABLE.CONTENT.like("%" + search.getMessage() + "%")));

        if (Objects.nonNull(startSeq) && Objects.isNull(endSeq))
            conditions.add(TABLE.SEQ.le(startSeq));
        else if (Objects.nonNull(endSeq) && Objects.isNull(startSeq))
            conditions.add(TABLE.SEQ.ge(endSeq));

        return conditions;
    }
}
