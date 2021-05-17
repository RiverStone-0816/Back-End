package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SoundEditorListenRequest extends BaseForm {
	@NotNull("음원")
	private String comment;
	@NotNull("재생속도")
	private Integer playSpeed;
}
