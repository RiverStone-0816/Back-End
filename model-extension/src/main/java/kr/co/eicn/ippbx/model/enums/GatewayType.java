package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * GwInfo Table
 * 서버유형(SIP: SIP소프트스위치, CPG:CPG, ETC:기타, TRK:트렁크, SBC:SBC)
 */
public enum GatewayType implements CodeHasable<String> {
	SIP_SOFT_SWITCH("SIP"), CPG("CPG"), ETC("ETC"), TRK("TRK"), SBC("SBC");

	private final String code;

	GatewayType(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
