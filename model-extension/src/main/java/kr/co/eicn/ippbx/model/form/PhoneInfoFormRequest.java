package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.model.enums.ForwardWhen;
import kr.co.eicn.ippbx.model.enums.RecordType;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.Size;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhoneInfoFormRequest extends BaseForm {
	@NotNull("내선번호")
	@Size(min = 3, max = 4)
	private String extension;
	@NotNull("070번호")
	private String number;
	@NotNull("전화기아이디")
	private String peer;
	@NotNull("전화기암호")
	private String passwd;
	@NotNull("게이트웨이")
	private String outboundGw;
	private String cid;
	@NotNull("지역번호")
	private String localPrefix = "02";
	@NotNull("녹취여부")
	private String recordType = RecordType.RECORDING.getCode(); // 녹취여부
	private String forwardWhen = ForwardWhen.NONE.getCode();         // 착신전환여부
	private String forwarding;                                       // 착신번호
	private String forwardNum;
	private String originalNumber;                                   // 번호이동원번호
	private String prevent;                                          // 금지프리픽스
	private String phoneKind = "N";								// 소프트폰 여부
	/**
	 * @see kr.co.eicn.ippbx.model.enums.ForwardKind
	 */
	private String forwardKind;
	private String type;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (!ForwardWhen.NONE.getCode().equals(forwardWhen)) {
			if (isEmpty(forwardKind) || isEmpty(forwardNum))
				reject(bindingResult, "forwardNum", "messages.validator.blank", "착신할 번호");
		}
		if (isNotEmpty(prevent)) {
			if (!Pattern.matches("^[0-9]{2,11}+([,][0-9]+)*$", prevent)) {
				reject(bindingResult, "forwardNum", "messages.validator.pattern", "금지프리픽스");
			}
		}
		if (isNotEmpty(number) && isNotEmpty(peer)) {
			if (!peer.equals(StringUtils.substring(number, 3))) {
				reject(bindingResult, "peer", "{전화아이디는 개인070번호에 의해 결정됩니다.}");
			}
		}

		return super.validate(bindingResult);
	}
}
