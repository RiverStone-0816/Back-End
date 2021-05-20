package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.entity.eicn.ScheduleInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class Number070ScheduleInfoResponse {
	private String number;        // 070번호
	private Byte    type;         // 번호타입(0:큐, 1:개인, 2:대표, 3:회의)
	private String  svcName;     // 대표번호 한글명
	private String  svcCid;      // 대표번호 CID번호
	private List<ScheduleInfoEntity> scheduleInfos; // 스케쥴러 일정 목록
}
