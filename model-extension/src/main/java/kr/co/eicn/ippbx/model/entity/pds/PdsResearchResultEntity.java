package kr.co.eicn.ippbx.model.entity.pds;

import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsResearchResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PdsResearchResultEntity extends PdsResearchResult {
    private PdsCustomInfo customInfo;
}
