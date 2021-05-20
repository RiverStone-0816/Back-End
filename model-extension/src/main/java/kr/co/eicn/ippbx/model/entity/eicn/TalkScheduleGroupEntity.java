package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleGroupEntity extends TalkScheduleGroup {
	private List<TalkScheduleGroupListEntity> scheduleGroupLists; // 스케쥴유형 목록
}
