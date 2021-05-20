package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonTalkMsg;
import kr.co.eicn.ippbx.model.entity.customdb.TalkMsgEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
}
