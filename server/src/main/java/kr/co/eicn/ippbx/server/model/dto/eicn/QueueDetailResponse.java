package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.enums.NoConnectKind;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueueDetailResponse extends QueueSummaryResponse {
	private String svcNumber;                           // 관련서비스
	private Integer maxlen = 0;                         // 최대대기자수 명수
	private Integer queueTimeout;                       // 큐 총대기시간
	private Integer timeout;                            // 사용자별 대기시간
	private String musiconhold;                         // 대기음원
	private String musiconholdName;                     // 음원명
	private Integer retryMaxCount = 0;
	private String retrySoundCode;
	private String ttsData;
	private String retryAction;
	private NoConnectKind noConnectKind = NoConnectKind.NONE;
	private String noConnectData;
	private String isForwarding;                        // 비상시 포워딩
	private String huntForwarding;                      // 비상시 포워딩 값
	private String groupCode;                          // 자신이 속한 조직코드
	private String blendingWaitExeYn;
	private Integer blendingWaitingCount;     		   //기준고객 대기자수
	private Integer blendingWaitingKeepTime;  		  //기준고객 초과후 유지시간
	private String blendingTimeExeYn;           //
	private Integer blendingTimeFromtime;
	private Integer blendingTimeTotime;

	private List<SummaryQueuePersonResponse> addPersons;     // 추가된 큐사용자
}
