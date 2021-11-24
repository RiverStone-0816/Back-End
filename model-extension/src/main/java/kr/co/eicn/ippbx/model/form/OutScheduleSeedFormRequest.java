package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutScheduleSeedFormRequest extends BaseForm {
	@NotNull("일정명")
	private String name;                // 일정명
	@NotNull("내선번호")
	private Set<String> extensions;     // 추가 내선번호
	@NotNull("요일")
	private Set<String> weeks;          // 요일
	@NotNull("시작시간(분)")
	private Integer fromhour;           // 시작시간 분 (09:00 -> 540)
	@NotNull("종료시간(분)")
	private Integer tohour;             // 종료시간 분 (18:00 -> 1080)
	private String soundCode;           // 음원 참조키

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (Objects.nonNull(fromhour) && Objects.nonNull(tohour)) {
			if (fromhour.compareTo(tohour) >= 0)
				reject(bindingResult, "fromhour", "{시작시간이 종료시간보다 이전이어야 합니다.}");
		}

		return super.validate(bindingResult);
	}
}
