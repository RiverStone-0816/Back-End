package kr.co.eicn.ippbx.model;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDSIvrComposite extends PdsIvr {
	private List<PDSIvrComposite> nodes;

	public boolean isLeaf() {
		return nodes == null || nodes.isEmpty();
	}
}
