package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.PatternUtils;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Data
public class DayOutScheduleSeedFormRequest extends BaseForm {
	@NotNull("일정명")
	private String name;                // 일정명
	@NotNull("내선번호")
	private Set<String> extensions;     // 추가 내선번호
	@NotNull("시작일")
	private String fromDate; // YYYY-MM-DD
	@NotNull("종료일")
	private String toDate; // YYYY-MM-DD
	@NotNull("시작시간(분)")
	private Integer fromhour;           // 시작시간 분 (09:00 -> 540)
	@NotNull("종료시간(분)")
	private Integer tohour;             // 종료시간 분 (18:00 -> 1080)
	private String soundCode;           // 음원 참조키

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(fromDate) && isNotEmpty(toDate))
			if (!PatternUtils.isDate(fromDate))
				reject(bindingResult, "fromDate", "{시작일을 형식에 맞게 입력하여 주세요.}", "시작일");
			else if (!PatternUtils.isDate(toDate))
				reject(bindingResult, "toDate", "{종료일을 형식에 맞게 입력하여 주세요.}", "종료일");
			else if (LocalDate.parse(fromDate).isAfter(LocalDate.parse(toDate)))
				reject(bindingResult, "fromDate", "{종료일은 시작일 이후로 설정해주세요.}");

		if (Objects.nonNull(fromhour) && Objects.nonNull(tohour)) {
			if (fromhour.compareTo(tohour) > 0)
				reject(bindingResult, "fromhour", "{종료시간은 시작시간 이후로 설정해주세요.}");
		}

		return super.validate(bindingResult);
	}
}
