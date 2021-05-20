package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.model.enums.ResearchItemSoundKind;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchItemFormRequest extends BaseForm {
	@NotNull("문항제목")
	private String  itemName;
	@NotNull("질문")
	private String  word;
	/**
	 * @see ResearchItemSoundKind
	 */
	private String  soundKind = ResearchItemSoundKind.NONE.getCode(); // 음원(ARS설문시사용)
	private Integer soundCode;
	private String  groupCode;

	private List<String> answerRequests;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(soundKind))
			if (ResearchItemSoundKind.SOUND.getCode().equals(soundKind))
				if (soundCode == null)
					reject(bindingResult, "soundCode", "{음원 설문사용시 음원은 필수 입력값입니다.}");
		if (answerRequests != null)
			if (answerRequests.size() >= 20)
				reject(bindingResult, "answerRequests", "{답변 갯수는 20개 미만으로 설정해 주세요.}");
			else {
				for (String answer : answerRequests) {
					if (isEmpty(answer))
						reject(bindingResult, "answerRequests", "messages.validator.blank");
				}
			}

		return super.validate(bindingResult);
	}
}
