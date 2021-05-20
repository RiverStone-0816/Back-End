package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleGroupListEntity extends ScheduleGroupList {
	private String kindSoundName; // 음원
	private String kindDataName; //
}
