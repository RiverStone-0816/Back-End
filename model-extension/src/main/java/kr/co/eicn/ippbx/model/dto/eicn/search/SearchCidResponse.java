package kr.co.eicn.ippbx.model.dto.eicn.search;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CidInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchCidResponse extends CidInfo {
}
