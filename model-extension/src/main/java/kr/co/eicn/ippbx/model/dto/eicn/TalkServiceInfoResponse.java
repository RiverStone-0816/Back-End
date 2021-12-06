package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleInfoEntity;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import lombok.Data;

import java.util.List;

@Data
public class TalkServiceInfoResponse {
	private String kakaoServiceName; // 상담톡 서비스명
	private String senderKey;        //상담톡 서비스키
	private TalkChannelType channelType;

	private List<TalkScheduleInfoEntity> scheduleInfos; // 스케쥴러 목록
}
