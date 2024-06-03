package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleInfoFormRequest extends BaseForm {
	private String groupCode; // 조직코드
	@NotNull("상담톡 서비스")
	private Set<String> senderKeys; //상담톡 서비스키
	@NotNull("스케쥴유형")
	private Integer groupId; // 스케쥴유형
	@NotNull("채널 타입")
	private TalkChannelType channelType;

	private Boolean              isEach = Boolean.FALSE;
	private Map<String, Integer> weeks;
}
