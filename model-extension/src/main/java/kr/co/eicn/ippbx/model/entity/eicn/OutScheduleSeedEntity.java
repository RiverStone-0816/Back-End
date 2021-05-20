package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.OutScheduleSeed;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutScheduleSeedEntity extends OutScheduleSeed {
	private List<OutScheduleListEntity> outScheduleLists; // 발신 스케쥴 일정 목록
}
