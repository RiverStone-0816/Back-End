package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SummaryQueuePersonResponse extends SummaryPersonResponse {
	private Integer   penalty;     // 스킬순서값
	private Integer   callRate;    // 콜배율값
}
