package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PDSGroupSummaryResponse {
	private Integer   seq;
	private String    name;             // 그룹명
	private Timestamp makeDate;         // 그룹생성일
	private Timestamp lastUploadDate; // 마지막업로드날짜
	private Integer   totalCnt;     // 업로드데이터수
	private Integer   uploadTryCnt; // 업로드횟수
	/**
	 * @see kr.co.eicn.ippbx.model.enums.PDSGroupUploadStatus
	 */
	private String    lastUploadStatus; // 마지막업로드상태
	private Timestamp lastExecuteDate;  // 마지막실행한날
	private Integer   executeTryCnt;    // 실행횟수
	/**
	 * @see kr.co.eicn.ippbx.model.enums.PDSGroupExecuteStatus
	 */
	private String    lastExecuteStatus; // 실행상태
}
