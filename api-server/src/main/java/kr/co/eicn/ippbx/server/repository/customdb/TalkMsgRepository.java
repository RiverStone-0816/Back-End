package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonTalkMsg;
import kr.co.eicn.ippbx.model.entity.customdb.TalkMsgEntity;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@Getter
public class TalkMsgRepository extends CustomDBBaseRepository<CommonTalkMsg, TalkMsgEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(TalkMsgRepository.class);
    private CommonTalkMsg TABLE;

    public TalkMsgRepository(String companyId) {
        super(new CommonTalkMsg(companyId), new CommonTalkMsg(companyId).SEQ, TalkMsgEntity.class);
        this.TABLE = new CommonTalkMsg(companyId);
    }

    public List<TalkMsgEntity> findAll(String roomId) {
        return super.findAll(TABLE.ROOM_ID.eq(roomId));
    }

    public List<TalkMsgEntity> getAllLastMessagesInRoomIds(Set<String> roomIdList) {
        final CommonTalkMsg table1 = TABLE.as("TABLE1");
        final CommonTalkMsg table2 = TABLE.as("TABLE2");

        return dsl.select(table1.ROOM_ID, table2.SEQ, table2.CONTENT, table2.TYPE, table2.SEND_RECEIVE)
                .from(
                        dsl.select(table1.ROOM_ID, DSL.max(table1.SEQ).as(table1.SEQ))
                                .from(table1)
                                .where(table1.ROOM_ID.ne("NO_SESSID"))
                                .groupBy(table1.ROOM_ID).asTable("TABLE1")
                ).join(table2)
                .on(table1.SEQ.eq(table2.SEQ)
                        .and(table1.ROOM_ID.eq(table2.ROOM_ID)))
                .where(table2.ROOM_ID.in(roomIdList))
                .fetchInto(TalkMsgEntity.class);
    }
}
