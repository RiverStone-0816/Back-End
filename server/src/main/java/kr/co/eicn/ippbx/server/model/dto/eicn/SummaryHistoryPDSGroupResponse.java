package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryHistoryPDSGroupResponse {
	private String    executeId; // 실행 아이디
	private String    executeName; // 실행명
	private String    pdsName; // 그룹명
}
