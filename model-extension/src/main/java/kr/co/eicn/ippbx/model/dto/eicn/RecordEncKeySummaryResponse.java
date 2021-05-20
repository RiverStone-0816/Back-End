package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RecordEncKeySummaryResponse {
	private Integer   id;           // 아이디
	private Timestamp createTime;   // 암호키 적용시간
	private String    encKey;       // 암호키
}
