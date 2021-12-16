package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TalkMsgSummaryResponse {
	private Integer seq;	//talk msg 시퀀스
	private TalkChannelType channelType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp insertTime;
	private String sendReceive;
	private String userId;
	private String userName;
	private String type;
	private String content;
}
