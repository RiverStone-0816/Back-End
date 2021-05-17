package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkScheduleGroupList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleGroupListEntity extends TalkScheduleGroupList {
	private String kindDataName;
}
