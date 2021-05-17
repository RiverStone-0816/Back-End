package kr.co.eicn.ippbx.server.model.entity.statdb;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.server.jooq.statdb.tables.pojos.StatVoc;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatVOCEntity extends StatVoc {
	private VocGroup group; // VOC그룹
}
