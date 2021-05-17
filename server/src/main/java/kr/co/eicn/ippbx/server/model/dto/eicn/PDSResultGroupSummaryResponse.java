package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PDSResultGroupSummaryResponse {
	private Integer   seq;
	private String    name;             // 큐그룹명(영문)
	private String hanName;         // 큐그룹명(한글)
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.PDSResultGroupStrategy
	 */
	private String strategy; // 분배정책
	private String   hostName;     // 교환기
	private Integer   cnt; // 사용자수
}
