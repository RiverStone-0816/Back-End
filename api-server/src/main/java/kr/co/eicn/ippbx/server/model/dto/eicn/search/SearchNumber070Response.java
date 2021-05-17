package kr.co.eicn.ippbx.server.model.dto.eicn.search;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchNumber070Response extends Number_070 {
	private String hostName;
	private String groupCode;
	private String groupTreeName;
	private Integer groupLevel;
}
