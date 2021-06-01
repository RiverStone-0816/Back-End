package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * common_type.status
 */
public enum CommonTypeStatus implements CodeHasable<String> {
	USING("U"), DELETE("D");

	private final String code;

	CommonTypeStatus(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
