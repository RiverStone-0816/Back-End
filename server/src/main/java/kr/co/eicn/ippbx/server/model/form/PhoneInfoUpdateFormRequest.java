package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.ForwardWhen;
import kr.co.eicn.ippbx.server.model.enums.RecordType;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.Size;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhoneInfoUpdateFormRequest extends BaseForm {
	@NotNull("기존전화기아이디")
	private String oldPeer;
	@NotNull("내선번호")
	@Size(min = 3, max = 4)
	private String extension;
	@NotNull("070번호")
	private String number;
	@NotNull("전화기아이디")
	private String peer;
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
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.ForwardKind
	 */
	private String forwardKind;
	private String type;
	private String billingNumber;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (!ForwardWhen.NONE.getCode().equals(forwardWhen)) {
			if (isEmpty(forwardKind) || isEmpty(forwardNum))
				reject(bindingResult, "forwardNum", "messages.validator.blank", "착신할 번호");
		}
		if (isNotEmpty(prevent)) {
			if (!Pattern.matches("^[0-9]{1,11}+([,][0-9]+)*$", prevent)) {
				reject(bindingResult, "forwardNum", "messages.validator.pattern", "금지프리픽스");
			}
		}
		return super.validate(bindingResult);
	}
}
