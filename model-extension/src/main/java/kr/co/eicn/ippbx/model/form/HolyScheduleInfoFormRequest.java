package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class HolyScheduleInfoFormRequest extends ScheduleInfoFormRequest {
	@Valid
	@NotNull("공휴일 기간")
	private List<PeriodDateFormRequest> periodDates;
}
