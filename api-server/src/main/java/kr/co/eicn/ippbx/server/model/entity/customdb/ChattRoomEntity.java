package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonChattRoom;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChattRoomEntity extends CommonChattRoom {
    private List<ChattRoomMemberEntity> chattingMembers;   //채팅방 멤버
    private List<ChattMsgEntity> chattingMessages;      //채팅방 메시지
}
