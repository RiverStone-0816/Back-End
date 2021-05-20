package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.time.DateTimeException;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;

@EqualsAndHashCode(callSuper = true)
@Data
public class HolyInfoFormRequest extends BaseForm {
	@NotNull("휴일명")
	private String  holyName;
	@NotNull("월")
	private Integer holyMonth;
	@NotNull("일")
	private Integer holyDay;
	private String  lunarYn; // 양력/음력(N:양력, Y:음력)

	@JsonIgnore
	public String getHolyDate() {
		return MonthDay.of(holyMonth, holyDay).format(DateTimeFormatter.ofPattern("M-dd"));
	}

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (holyMonth != null && holyDay != null) {
			try {
				MonthDay.of(holyMonth, holyDay);
			} catch (DateTimeException e) {
				reject(bindingResult, "holyMonth", "{날짜를 확인해 주세요.}");
				reject(bindingResult, "holyDay", "{날짜를 확인해 주세요.}");
			}
		}

		return super.validate(bindingResult);
	}
}
