package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VOCResultResponse extends VocResult {
	private String    idName; // 상담사
	private SummaryResearchListResponse research; // 설문
	private String vocResultLocal; // 지역(1단계)
	private String age; // 나이(2단계)
	private String aa; // 만족도(3단계)
}
