package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList;
import kr.co.eicn.ippbx.model.ResearchQuestionItemComposite;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchListEntity extends ResearchList {
	private ResearchQuestionItemComposite scenario;
}
