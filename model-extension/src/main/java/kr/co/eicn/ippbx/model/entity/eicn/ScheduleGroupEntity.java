package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleGroupEntity extends ScheduleGroup {
	private List<ScheduleGroupListEntity> scheduleGroupLists; // 스케쥴유형 목록
}
