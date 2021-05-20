package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryTalkServiceResponse {
	private String  senderKey;            // 상담톡키
	private String  kakaoServiceName;     // 상담톡 서비스명
}
