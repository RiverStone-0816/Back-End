package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkScheduleGroupEntity extends WtalkScheduleGroup {
	private List<WtalkScheduleGroupListEntity> scheduleGroupLists; // 스케쥴유형 목록
}
