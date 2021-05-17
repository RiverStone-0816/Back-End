package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryCommonFieldResponse {
	private Integer type; // common_type.seq 참조 키
	private String  fieldId;
	private String  fieldInfo;
}
