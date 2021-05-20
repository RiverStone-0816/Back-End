package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattMsgRead;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.count;

@Getter
public class ChattMsgReadRepository extends CustomDBBaseRepository<CommonChattMsgRead, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattMsgRead, String>  {
    protected final Logger logger = LoggerFactory.getLogger(ChattMsgReadRepository.class);

    private final CommonChattMsgRead TABLE;

    public ChattMsgReadRepository(String companyId) {
        super(new CommonChattMsgRead(companyId), new CommonChattMsgRead(companyId).MESSAGE_ID, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattMsgRead.class);
        this.TABLE = new CommonChattMsgRead(companyId);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattMsgRead> findAll(String roomId) {
        return findAll(TABLE.ROOM_ID.eq(roomId));
    }

    public Map<String, Integer> getUnreadMessageCount(String roomId) {
        return dsl.select(TABLE.MESSAGE_ID, count())
                .from(TABLE)
                .where(TABLE.ROOM_ID.eq(roomId))
                .and(TABLE.USERID.ne(g.getUser().getId()))
                .groupBy(TABLE.MESSAGE_ID)
                .fetchMap(TABLE.MESSAGE_ID, count());
    }

    public Map<String, Integer> getUnreadMessageTotalCount() {
        return dsl.select(TABLE.ROOM_ID, count())
                .from(TABLE)
                .where(TABLE.USERID.eq(g.getUser().getId()))
                .groupBy(TABLE.ROOM_ID)
                .fetchMap(TABLE.ROOM_ID, count());
    }
}

