package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatWtalk implements Serializable {
	private Integer seq;
	private String  companyId;
	private String  channelType;
	private String  senderKey;
	private String  userid;
	private String  groupCode;
	private String  groupTreeName;
	private Integer groupLevel;
	private String  worktimeYn;
	private Date    statDate;
	private Byte    statHour;
	private String  actionType;
	private Integer cnt;
}
