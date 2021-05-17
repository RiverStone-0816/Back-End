package kr.co.eicn.ippbx.server.model.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class PeriodDateFormRequest extends BaseForm {
	@NotNull("시작일")
	private String fromDate; // yyyy-MM-dd
	@NotNull("종료일")
	private String toDate; // yyyy-MM-dd

	@JsonIgnore
	@AssertTrue(message = "종료시각이 시작시각 이후여야 합니다.")
	public boolean isPeriodValid() {
		if (isNotEmpty(fromDate) && isNotEmpty(toDate)) {
			try {
				final Period between = Period.between(LocalDate.parse(fromDate), LocalDate.parse(toDate));
				if (between.isNegative())
					return false;
			} catch (DateTimeParseException e) {
				return false;
			}
		}
		return true;
	}
}
