package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 상담원 근무상태
 *  근무상태("": 정상근무, S:휴직, X:퇴직)
 */
public enum IdStatus implements CodeHasable<String > {
	WORKING(""), LEAVE("S"), RETIRE("X");

	private final String code;

	IdStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
