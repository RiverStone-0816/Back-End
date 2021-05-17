package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.server.model.enums.CommonTypePurpose;
import kr.co.eicn.ippbx.server.util.EnumUtils;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonTypeFormRequest extends BaseForm {
	@NotNull("유형명")
	private String name;  // 유형명
	/**
	 *@see CommonTypeKind
	 */
	@NotNull("유형타입")
	private String kind;  // 유형타입
	private String etc;   // 유형정보
	/**
	 * @see CommonTypePurpose
	 */
	private String purpose; // 용도지정 구분
	private String type;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (StringUtils.isEmpty(kind))
			if (Objects.isNull(EnumUtils.of(CommonTypeKind.class, kind)))
				reject(bindingResult, "kind", "{유형타입이 입력되지 않았습니다.}", "");

		return super.validate(bindingResult);
	}
}
