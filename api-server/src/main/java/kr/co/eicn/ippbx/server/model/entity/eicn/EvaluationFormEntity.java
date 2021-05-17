package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationFormEntity extends EvaluationForm {
	private List<EvaluationCategoryEntity> categories = Collections.emptyList();
}
