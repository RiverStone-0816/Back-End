package kr.co.eicn.ippbx.model.dto.eicn.search;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchNumber070Response extends Number_070 {
	private String hostName;
}
