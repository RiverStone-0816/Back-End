package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NumberTypeChangeRequest extends BaseForm {
	/**
	 * @see kr.co.eicn.ippbx.model.enums.NumberType
	 */
	@NotNull("번호타입")
	private Byte type;
}
