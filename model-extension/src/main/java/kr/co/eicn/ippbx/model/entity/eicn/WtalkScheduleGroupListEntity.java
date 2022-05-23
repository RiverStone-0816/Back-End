package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroupList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkScheduleGroupListEntity extends WtalkScheduleGroupList {
	private String kindDataName;
}
