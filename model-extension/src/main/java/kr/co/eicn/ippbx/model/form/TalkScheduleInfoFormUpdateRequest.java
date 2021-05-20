package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleInfoFormUpdateRequest extends BaseForm {
	private String groupCode; // 조직코드
	@NotNull("스케쥴유형")
	private Integer groupId; // 스케쥴유형
}
