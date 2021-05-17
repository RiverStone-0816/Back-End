package kr.co.eicn.ippbx.server.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatTalk implements Serializable {
	private Integer seq;
	private String  senderKey;
	private String  userid;
	private String  groupCode;
	private String  groupTreeName;
	private Integer groupLevel;
	private Date    statDate;
	private Byte    statHour;
	private Integer startRoomCnt;
	private Integer endRoomCnt;
	private Integer inMsgCnt;
	private Integer outMsgCnt;
	private Integer autoMentCnt;
	private Integer autoMentExceedCnt;
	private Integer roomTimeSum;
}
