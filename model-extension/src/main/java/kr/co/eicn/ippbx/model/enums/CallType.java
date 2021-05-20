package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

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
