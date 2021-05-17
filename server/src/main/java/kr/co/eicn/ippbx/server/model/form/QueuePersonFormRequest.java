package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class QueuePersonFormRequest {
	@NotNull("전화기 ID")
	private String peer;
	private Integer   penalty;     // 스킬순서값
	private Integer   callRate;    // 콜배율값
}
