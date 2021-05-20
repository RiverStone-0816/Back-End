package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryTalkScheduleInfoResponse {
	private Integer parent; // 스케쥴유형 키
	private String name; // 스케쥴유형명
}
