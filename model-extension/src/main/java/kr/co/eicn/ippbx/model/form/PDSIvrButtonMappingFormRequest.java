package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.model.enums.Button;
import kr.co.eicn.ippbx.model.enums.IvrTreeType;
import kr.co.eicn.ippbx.model.enums.IvrTreeTypeGroup;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.AssertTrue;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class PDSIvrButtonMappingFormRequest extends BaseForm {
	private Integer seq;
	@NotNull("IVR명")
	private String name;
	/**
	 * @see kr.co.eicn.ippbx.model.enums.Button
	 */
	@NotNull("버튼과 매핑되는 정보")
	private String button;

	/**
	 * @see kr.co.eicn.ippbx.model.enums.IvrTreeType
	 * @see IvrTreeTypeGroup
	 */
	@NotNull("연결설정")
	private Byte    type;

	/**
	 *  연결설정이 PDS헌트연결 시 PDS_QUEUE_NAME 정보
	 *  음원플레이 설정 시 사운드 정보가 저장된다.
	 */
	private String  typeData;

	@JsonIgnore
	@AssertTrue(message = "올바르지 않은 입력값")
	public boolean isButtonConverter() {
		return Objects.nonNull(EnumUtils.of(Button.class, button));
	}

	@JsonIgnore
	@AssertTrue(message = "올바르지 않은 입력값")
	public boolean isTypeConverter() {
		return Objects.equals(IvrTreeTypeGroup.PDS_IVR, IvrTreeTypeGroup.findByIvrTreeTypeGroup(IvrTreeTypeGroup.PDS_IVR, EnumUtils.of(IvrTreeType.class, type)));
	}

	@JsonIgnore
	@AssertTrue(message = "PDS헌트연결 및 음원플레이 설정 시 연결데이터 정보는 필수 입력사항입니다.")
	public boolean isNotEmptyTypeData() {
		if (IvrTreeType.CALL_CONNECTION_BY_CONNECTION_NUMBER.getCode().equals(type) || IvrTreeType.RECORD_PLAY.getCode().equals(type))
			return !isEmpty(typeData);
		return true;
	}
}
