package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class PhoneInfoSummaryResponse {
	private String  peer;        //전화기아이디
	private String  companyId;
	private String  extension;   // 내선번호
	private String  voipTel;     // 개인070번호
	private String  forwardWhen; // 착신전환여부
	private String  forwarding;  // 착신할전화번호
	private String  fwWhen;
	private String  fwKind;
	private String  fwNum;
	private String  recordType;   // 녹취 여부
	private String  cid;          // 발신번호
	private String  billingNumber;// 과금번호
	private String  localPrefix;  // 지역번호
	private String  originalNumber; // 번호이동원번호
}
