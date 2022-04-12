package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.enums.TalkRoomMode;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TalkCurrentListResponse {
    private Integer seq;    //talk room 시퀀스
    private TalkChannelType channelType;
    private String roomId;
    private String roomName;
    private String roomStatus;
    private TalkRoomMode talkRoomMode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp roomStartTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp roomLastTime;
    private Integer lastMessageSeq;
    private String userId;
    private String userName;
    private String userKey;
    private String senderKey;
    private String svcName = ""; //sender_key로 TalkServiceInfoDB해서 getKakaoServiceName()
    private String content;    //TalkMsg.getMessage
    private String type;    //TalkMsg.getType
    private String send_receive; //TalkMsg.getSnedReceive
    private Integer maindbGroupId;
    private String maindbCustomName;
    private String maindbCustomId;
}
