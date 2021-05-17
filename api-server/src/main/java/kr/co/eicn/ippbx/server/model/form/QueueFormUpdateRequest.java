package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.model.enums.ForwardingType;
import kr.co.eicn.ippbx.server.model.enums.NoConnectKind;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueueFormUpdateRequest extends BaseForm {
	private String name;                       // QUEUE 이름
	@NotNull("큐그룹명")
	private String hanName;
	@NotNull("큐번호")
	private String number;
	private String svcNumber;               // 관련서비스
	@NotNull("조직코드")
	private String groupCode;
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.CallDistributionStrategy
	 */
	@NotNull("통화분배정책")
	private String strategy;
	@NotNull("최대대기자명수")
	private Integer maxlen = 0;
	@NotNull("큐 총대기시간")
	private Integer queueTimeout = 60;
	@NotNull("사용자별 대기시간")
	private Integer timeout = 15;
	private String musiconhold;             // 대기음원
	@NotNull("재시도 여부")
	private Boolean isRetry = Boolean.FALSE;
	private Integer retryMaxCount = 0;	    //재시도 횟수
	private String retrySoundCode;   		//재시도 음원
	private String ttsData;			//재시도 버튼
	private String retryAction;
	@NotNull("비연결시 행동")
	private NoConnectKind noConnectKind = NoConnectKind.NONE;
	private String noConnectData;   //연결데이터
	@NotNull("비상시 포워딩")
	private String isForwarding = "N";
	private String huntForwarding;          // 비상시 포워딩 값

	private List<QueuePersonFormRequest> addPersons; // 추가 사용자

//	private String    blendingMode;         // 블랜딩 모드

	@SneakyThrows
	@Override
	public boolean validate(BindingResult bindingResult) {
		if (timeout != null && timeout < 15)
			reject(bindingResult, "timeout", "validator.minimum", "사용자별 대기시간", 14);
		if (ForwardingType.INNER.getCode().equals(isForwarding) || ForwardingType.OUTER.getCode().equals(isForwarding))
			if (isEmpty(huntForwarding))
				reject(bindingResult, "huntForwarding", "내부 포워딩 및 외부 포워딩할 번호를 선택하여 주세요.", "");
		if (addPersons != null && addPersons.size() > 0)
			for (QueuePersonFormRequest addPerson : addPersons) {
				if (isEmpty(addPerson.getPeer())) {
					reject(bindingResult, "addPersons", "내선 전화기 ID를 선택하여 주세요.", "");
					break;
				}
			}
		if (Boolean.TRUE.equals(isRetry)) {
			if (retryMaxCount != null && retryMaxCount > 0) {
				if (isEmpty(retrySoundCode))
					reject(bindingResult, "retrySoundCode", "{재시도 음원을 선택하여 주세요.}", "");
			} else
				reject(bindingResult, "retryMaxCount", "{재시도 횟수를 선택하여 주세요.}", "");
		}

		return super.validate(bindingResult);
	}
}
