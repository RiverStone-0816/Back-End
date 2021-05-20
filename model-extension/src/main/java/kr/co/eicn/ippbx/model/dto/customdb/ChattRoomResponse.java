package kr.co.eicn.ippbx.model.dto.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom;
import lombok.Data;

@Data
public class ChattRoomResponse {
    private CommonChattRoom chattRoom;
    private Integer unreadMessageTotalCount = 0;        //채팅방 별 안 읽은 메시지 개수
}
