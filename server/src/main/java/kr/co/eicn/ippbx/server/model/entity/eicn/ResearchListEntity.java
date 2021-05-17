package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchList;
import kr.co.eicn.ippbx.server.model.ResearchQuestionItemComposite;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchListEntity extends ResearchList {
	private ResearchQuestionItemComposite scenario;
}
