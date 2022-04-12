package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonTalkRoom;
import kr.co.eicn.ippbx.model.enums.TalkRoomMode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkRoomEntity extends CommonTalkRoom {
    private TalkRoomMode talkRoomMode;
}
