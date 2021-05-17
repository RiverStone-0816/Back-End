package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.jooq.eicn.enums.EvaluationFormUseType;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationFormRequest extends BaseForm {
	@NotNull("평가지명")
	private String                name;
	private Date                  startDate;
	private Date                  endDate;
	@NotNull("진행여부")
	private EvaluationFormUseType useType; // N:미진행, IN_PROGRESS:진행중, CLOSED:종료, PERIOD:기간설정
	private String                memo;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (Objects.equals(EvaluationFormUseType.PERIOD, useType)) {
			if (startDate == null)
				reject(bindingResult, "startDate", "진행여부가 기간설정일경우 진행기간은 필수입력사항입니다.");
			if (endDate == null)
				reject(bindingResult, "endDate", "진행여부가 기간설정일경우 진행기간은 필수입력사항입니다.");
		}

		return super.validate(bindingResult);
	}
}
