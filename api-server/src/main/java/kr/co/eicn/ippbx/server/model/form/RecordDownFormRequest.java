package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecordDownFormRequest extends BaseForm {
	@NotNull("다운명")
	private String downName; // 다운명
	@NotNull("시퀀스")
	private List<Integer> sequences;
}
