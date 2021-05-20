package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import kr.co.eicn.ippbx.util.valid.Range;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonFieldFormRequest extends BaseForm {
	@NotNull("필드 아이디")
	private String  id;             // COMMON_BASIC_FIELD.ID
	@NotNull("필드명")
	private String  fieldName;      // 필드명
	@NotNull("필드사이즈")
	@Range(min = 0, max = 500)
	private Integer fieldSize;      // 필드사이즈(500자이내)
	@NotNull("필수여부")
	private String isneed;            // 필수여부(Y:N)
	@NotNull("상담원 노출여부")
	private String isdisplay;         // 상담원 노출여부(Y:N)
	@NotNull("리스트 노출여부")
	private String isdisplayList;     // 리스트 노출여부(Y:N)
	@NotNull("검색여부")
	private String issearch;          // 검색여부(Y:N)
}
