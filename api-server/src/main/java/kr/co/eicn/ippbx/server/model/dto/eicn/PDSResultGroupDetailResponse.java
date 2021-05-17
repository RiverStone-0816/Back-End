package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDSResultGroupDetailResponse extends PDSResultGroupSummaryResponse {
	private String busyContext;                         // 비연결시 컨텍스트
	private List<SummaryQueuePersonResponse> addPersons;     // 추가된 헌트사용자
}
