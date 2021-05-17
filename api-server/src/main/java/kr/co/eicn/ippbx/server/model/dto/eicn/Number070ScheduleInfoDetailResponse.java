package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.entity.eicn.ScheduleInfoEntity;
import lombok.Data;

@Data
public class Number070ScheduleInfoDetailResponse {
	private String number;        // 070번호
//	private Byte    type;         // 번호타입(0:큐, 1:개인, 2:대표, 3:회의)

	private ScheduleInfoEntity scheduleInfo; // 스케쥴 일정 정보
}
