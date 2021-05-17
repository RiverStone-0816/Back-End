package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonTypeUpdateFormRequest extends CommonTypeFormRequest {
	@Valid
	@NotNull("필드등록 정보 목록")
	private List<CommonFieldFormRequest> fieldFormRequests; // 필드등록 정보 목록
}
