package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VocGroupEntity extends VocGroup {
	private ResearchList research; // 설문정보
}
