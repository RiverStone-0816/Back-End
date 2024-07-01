package kr.co.eicn.ippbx.model.form;

import io.jsonwebtoken.lang.Collections;
import kr.co.eicn.ippbx.model.enums.IsWebVoiceYn;
import kr.co.eicn.ippbx.model.enums.IvrMenuType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class IvrFormRequest extends BaseForm {
	/**
	 * 상위노드 연결시
	 */
	private Integer parentSeq;
	private Integer rootSeq;
	@NotNull("메뉴")
	private String name;           // IVR명
	private String introSoundCode; // 인트로음원
	private String soundCode;       // 음원선택
	/**
	 * @see kr.co.eicn.ippbx.model.enums.IsWebVoiceYn
	 */
	private String isWebVoice = IsWebVoiceYn.WEB_VOICE_N.getCode();     // 보이는 ARS사용
	private Integer posX = 0;       // 화면에서 x축 좌표
	private Integer posY = 0;       // 화면에서 y축 좌표
	private Integer copyToSourceId; // 복사할 아이디

	/**
	 * @see kr.co.eicn.ippbx.model.enums.IvrMenuType
	 */
	@NotNull("연결설정")
	private Byte type;

	/**
	 * -- 타입이 헌트번호연결일 경우
	 * list[0]:헌트번호, list[1]:재시도횟수, list[2]:재시도음원, list[3]:비연결시컨텍스트 정보가 저장된다.
	 */
	private List<String> typeDataStrings;
	/**
	 * -- 인트로음원, 음원, 재시도음원 선택시 TTS를 선택할 경우
	 * list[0]: TTS문구, list[1]: TTS문구, list[2]: TTS문구
	 */
	private List<String> ttsDataStrings;

	private List<IvrButtonMappingFormRequest> buttons;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(soundCode)) {
			if ("TTS".equals(soundCode)) {
				if (Collections.isEmpty(ttsDataStrings))
					reject(bindingResult, "ttsDataStrings", "messages.validator.blank");
			}
		}
		if (isNotEmpty(introSoundCode)) {
			if ("TTS".equals(introSoundCode)) {
				if (Collections.isEmpty(ttsDataStrings))
					reject(bindingResult, "ttsDataStrings", "messages.validator.blank");
			}
		}
		if (type != null) {
			final IvrMenuType treeType = IvrMenuType.of(type);
			if (treeType == null)
				reject(bindingResult, "type", "messages.validator.invalid", type);

			if (Arrays.asList(1, 2, 7, 10).contains(type.intValue()) && StringUtils.isEmpty(soundCode))
				reject(bindingResult, "soundCode", "messages.validator.invalid", "soundCode");

			if (Arrays.asList(1, 7).contains(type.intValue()) && (buttons != null && !buttons.isEmpty())) {
				for (int i = 0; i < buttons.size(); i++) {
					buttons.get(i).validate("buttons[" + i + "].", bindingResult);
				}
			}
		}

		return super.validate(bindingResult);
	}
}
