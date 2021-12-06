package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleGroupFormRequest extends BaseForm {
	@NotNull("스케쥴 유형명")
	private String name;   // 스케쥴 유형명
	@NotNull("채널 타입")
	private TalkChannelType channelType;
}
