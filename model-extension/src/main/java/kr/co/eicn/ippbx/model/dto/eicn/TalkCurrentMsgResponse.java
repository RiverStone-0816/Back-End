package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import lombok.Data;

import java.util.List;

@Data
public class TalkCurrentMsgResponse {
	private Integer lastMsgSeq;
	private String roomStatus;
	private String roomName;
	private String customName;
	private TalkChannelType channelType;
	private List<TalkMsgSummaryResponse> talkMsgSummaryList;
	private String roomId;
	private String userKey;
	private String senderKey;
	private String userId;
	private String isAutoEnable;
}
