package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.DataSearchAuthorityType;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.enums.RecordingAuthorityType;
import kr.co.eicn.ippbx.util.PatternUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonFormUpdateRequest extends BaseForm {
	/**
	 * @see IdType
	 */
	@NotNull("상담원 유형")
	private String idType;              // 아이디유형구분
	@NotNull("성명")
	private String idName;              // 성명
	private String password;            // 패스워드 규칙:문자,숫자,특수문자의 조합으로 9~20자리
	@NotNull("조직코드")
	private String groupCode;            // 조직코드
	/**
	 * @see kr.co.eicn.ippbx.model.enums.PersonPausedStatus
	 */
	private String idStatus;          // 근무상태
	private String isPds;             // PDS 사용여부
	private String isTalk;           // 상담톡 여부
	private String isEmail;         // 이메일상담 여부
	private String isStat;           // 통계,모니터링
	private String isCti;           // 상담원연결여부
	private String isChatt;           // 메신저 사용 여부
	private String isAstIn;		   // 통합상담어시스트
	private String isAstOut;		   // 독립상담어시스트
	private String isAstStt;		   // 상담어시스트STT

	/**
	 * @see kr.co.eicn.ippbx.model.enums.RecordingAuthorityType
	 */
	private String listeningRecordingAuthority = RecordingAuthorityType.NONE.getCode(); // 녹취권한 듣기
	private String downloadRecordingAuthority = RecordingAuthorityType.NONE.getCode(); // 녹취권한 다운
	private String removeRecordingAuthority = RecordingAuthorityType.NONE.getCode(); // 녹취권한-삭제
	/**
	 * @see DataSearchAuthorityType
	 */
	private String dataSearchAuthority = DataSearchAuthorityType.NONE.getCode();  //데이터 검색 권한

	private String extension;       // 내선번호
	private String hpNumber;   // 휴대전화번호
	private String emailInfo;           // 이메일

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(idType)) {
			if (Objects.isNull(IdType.of(idType)))
				reject(bindingResult, "idType", "messages.validator.invalid", idType);
			if (IdType.MASTER.getCode().equals(idType))
				reject(bindingResult, "idType", "messages.validator.invalid", "아이디유형");
		}

		if (isNotEmpty(password)) {
			if (!PatternUtils.isPasswordValid(password))
				reject(bindingResult, "password", "{비밀번호는 문자, 숫자, 특수문자의 조합으로 9~20자리로 입력해주세요.}");
			if (PatternUtils.isPasswordSameCharacter(password))
				reject(bindingResult, "password", "{동일문자를 3번 이상 사용할 수 없습니다.}");
			if (PatternUtils.isPasswordContinuousCharacter(password))
				reject(bindingResult, "password", "{연속된 문자열(123 또는 321, abc, cba 등)을 3자 이상 사용 할 수 없습니다.}");
		}

		if (isNotEmpty(extension))
			if (!NumberUtils.isDigits(extension))
				reject(bindingResult, "extension", "messages.validator.invalid", extension);
		if (isNotEmpty(hpNumber))
			if (!PatternUtils.isPhoneNumber(hpNumber))
				reject(bindingResult, "hpNumber", "messages.validator.invalid", hpNumber);
		if (isNotEmpty(emailInfo))
			if (!PatternUtils.isEmail(emailInfo))
				reject(bindingResult, "emailInfo", "messages.validator.invalid", emailInfo);

		return super.validate(bindingResult);
	}
}
