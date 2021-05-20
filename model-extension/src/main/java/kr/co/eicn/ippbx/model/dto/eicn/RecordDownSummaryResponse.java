package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RecordDownSummaryResponse {
	private Integer   seq;         // SEQUENCE KEY
	private Timestamp requestdate; // 실행일
	private String    status;    // 파일 상태
	private String    downName; // 다운로드명
	private String    userName; // 실행자
	private String    userid;   // 실행자ID
	private String    downFolder; // 파일명
	private Long size;        // 파일 사이즈
}
