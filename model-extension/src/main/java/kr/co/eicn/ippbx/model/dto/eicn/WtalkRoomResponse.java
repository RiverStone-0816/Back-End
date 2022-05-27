package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import lombok.Data;

@Data
public class WtalkRoomResponse {
    private Integer seq;
    private TalkChannelType channelType;
    private String roomStartTime;   //시작시간
    private String roomLastTime;    //마지막메시지시간
    private String roomId;          //대화방 ID
    private String roomName;        //대화방명
    private String senderKey;       //상담톡 서비스
    /**
     * @see kr.co.eicn.ippbx.model.enums.RoomStatus
     * */
    private String roomStatus;      //대화방상태
    private String userKey;
    private String maindbCustomName;    //고객명
    private String idName;      //담당자
}
