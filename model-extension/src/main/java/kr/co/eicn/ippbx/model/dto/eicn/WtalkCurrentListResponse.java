package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.enums.TalkRoomMode;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class WtalkCurrentListResponse {
    private Integer         seq;
    private String          roomId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp       roomStartTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp       roomLastTime;
    private String          roomStatus;
    private TalkRoomMode    talkRoomMode;
    private Integer         maindbGroupId;
    private String          maindbCustomId;
    private String          maindbCustomName;
    private String          userId;
    private String          userKey;
    private TalkChannelType channelType;
    private String          senderKey;
    private String          roomName;
    private String          isAutoEnable;
    private String          isCustomUploadEnable;
    private String          userName;
    private String          svcName = ""; //sender_key로 TalkServiceInfoDB해서 getKakaoServiceName()
    private Integer         audioUseCnt;
    private Integer         videoUseCnt;
    private Integer         keyId;
    private String          lastUserYn;
    private String          lastType;
    private String          lastContent;
    private String          lastSendReceive;
}
