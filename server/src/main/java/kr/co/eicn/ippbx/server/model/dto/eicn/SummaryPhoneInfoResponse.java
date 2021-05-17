package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryPhoneInfoResponse {
	private String  extension; // 내선번호
	private String inUseIdName; // 사용중인 상담원명
}
