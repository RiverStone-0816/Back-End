package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonWtalkRoom;
import kr.co.eicn.ippbx.model.enums.TalkRoomMode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkRoomEntity extends CommonWtalkRoom {
    private TalkRoomMode talkRoomMode;
}
