package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryScheduleGroupResponse {
	private Integer parent;  // 스케쥴유형 키
	private String name;     // 스케쥴유형명
}
