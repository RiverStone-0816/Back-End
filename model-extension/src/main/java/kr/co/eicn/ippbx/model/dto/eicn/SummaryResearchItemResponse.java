package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryResearchItemResponse {
	private Integer seq;     // 조회시
	private Integer itemId; // 시나리오 참조 데이터
	private String  itemName;
	private String  soundKind;
}
