package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleInfoFormRequest extends BaseForm {
	private String groupCode; // 조직코드
	@NotNull("서비스")
	private Set<String> numbers; //서비스키
	@NotNull("스케쥴유형")
	private Integer groupId; // 스케쥴유형
}
