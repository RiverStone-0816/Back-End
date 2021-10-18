package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class TalkServiceSummaryResponse {
	private Integer seq;                  // SEQUENCE KEY
	private String kakaoServiceName;     // 상담톡 서비스명
	private String kakaoServiceId;        // 상담톡아이디
	private String senderKey;            // 상담톡키
	private String isChattEnable;        // 상담창활성화
	private String botId;
	private String botName;
}
