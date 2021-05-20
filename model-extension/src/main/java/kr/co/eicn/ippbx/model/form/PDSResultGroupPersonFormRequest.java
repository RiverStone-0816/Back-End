package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;

@Data
public class PDSResultGroupPersonFormRequest {
	@NotNull("상담원 ID")
	private String userId;
}
