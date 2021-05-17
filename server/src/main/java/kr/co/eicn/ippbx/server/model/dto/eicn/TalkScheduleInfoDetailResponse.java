package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.entity.eicn.TalkScheduleInfoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleInfoDetailResponse extends TalkScheduleInfoEntity {
	private String kakaoServiceName; // 상담톡 서비스명
	private String senderKey;        //상담톡 서비스키
	private String  isChattEnable; // 상담톡 활성화여부
}
