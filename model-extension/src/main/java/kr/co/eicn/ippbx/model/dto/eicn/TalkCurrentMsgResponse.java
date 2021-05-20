package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class TalkCurrentMsgResponse {
	private Integer lastMsgSeq;
	private String roomStatus;
	private String roomName;
	private String customName;
	private List<TalkMsgSummaryResponse> talkMsgSummaryList;

	private String roomId;
	private String userKey;
	private String senderKey;

	private String userId;
}
