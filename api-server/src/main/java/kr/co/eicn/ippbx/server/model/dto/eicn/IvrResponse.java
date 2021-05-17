package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class IvrResponse extends IvrTree {
	private List<IvrResponse> nodes;
}
