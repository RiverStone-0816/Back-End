package kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonChattRoomMember implements Serializable {
    private String    roomId;
    private String    userid;
    private String    roomName;
    private String    memberMd5;
    private String    isJoin;
    private Timestamp inviteTime;
    private String    lastMsg;
    private String    lastType;
    private Timestamp lastTime;
    private Timestamp readTime;
}
