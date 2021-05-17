package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum CallType implements CodeHasable<String> {
	INBOUND("I"), OUTBOUND("O");
	private String code;

	CallType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
