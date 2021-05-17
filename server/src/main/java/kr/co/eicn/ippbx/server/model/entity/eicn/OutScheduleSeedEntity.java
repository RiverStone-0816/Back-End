package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.OutScheduleSeed;
import lombok.Data;

import java.util.List;

@Data
public class OutScheduleSeedEntity extends OutScheduleSeed {
	private List<OutScheduleListEntity> outScheduleLists; // 발신 스케쥴 일정 목록
}
