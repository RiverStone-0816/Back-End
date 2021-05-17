package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Data
public class PDSResultGroupUpdateFormRequest extends BaseForm {
	private String name;                       // QUEUE 이름
	@NotNull("큐그룹명")
	private String hanName;
	@NotNull("실행할 교환기")
	private String    runHost;      // 실행할 교환기
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.PDSResultGroupStrategy
	 */
	@NotNull("통화분배정책")
	private String strategy;               // 분배정책
	private String busyContext;             // 비연결시 컨텍스트
	private List<PDSResultGroupPersonFormRequest> addPersons; // 추가 사용자

	@SneakyThrows
	@Override
	public boolean validate(BindingResult bindingResult) {
		if (addPersons != null && addPersons.size() > 0)
			for (PDSResultGroupPersonFormRequest addPerson : addPersons) {
				if (isEmpty(addPerson.getUserId())) {
					reject(bindingResult, "addPersons", "messages.validator.blank", "내선 전화기 ID");
					break;
				}
			}

		return super.validate(bindingResult);
	}
}
