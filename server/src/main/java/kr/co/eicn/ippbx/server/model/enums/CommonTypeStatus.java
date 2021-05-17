package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * common_type.status
 */
public enum CommonTypeStatus implements CodeHasable<String> {
	USING("U"), DELETE("D");

	private String code;

	CommonTypeStatus(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
