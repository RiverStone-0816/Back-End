package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkMemberGroupFormRequest extends BaseForm {
	@NotNull("상담톡그룹명")
	private String groupName;
	@NotNull("관련상담톡서비스")
	private String senderKey;
	@NotNull("추가 사용자")
	private Set<String> personIds; // 추가 사용자
}
