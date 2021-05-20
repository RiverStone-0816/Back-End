package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Number070Entity extends Number_070 {
	private List<ScheduleInfoEntity> scheduleInfos; // 스케쥴러 목록
}
