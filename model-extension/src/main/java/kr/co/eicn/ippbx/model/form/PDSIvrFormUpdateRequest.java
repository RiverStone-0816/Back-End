package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class PDSIvrFormUpdateRequest extends PDSIvrFormRequest {
	@Valid
	@NotNull("버튼 매핑 정보")
	private List< PDSIvrButtonMappingFormRequest> buttonMappingFormRequests;
}
