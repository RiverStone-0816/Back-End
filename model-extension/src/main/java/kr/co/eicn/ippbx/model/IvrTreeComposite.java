package kr.co.eicn.ippbx.model;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class IvrTreeComposite extends IvrTree {
	private List<IvrTreeComposite> nodes;

	public boolean isLeaf() {
		return nodes == null || nodes.isEmpty();
	}
}
