package kr.co.eicn.ippbx.model.entity.statdb;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.StatVoc;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatVOCEntity extends StatVoc {
	private VocGroup group; // VOC그룹
}
