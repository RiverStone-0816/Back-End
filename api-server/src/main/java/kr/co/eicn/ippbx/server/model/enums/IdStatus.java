package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 상담원 근무상태
 *  근무상태("": 정상근무, S:휴직, X:퇴직)
 */
public enum IdStatus implements CodeHasable<String > {
	WORKING(""), LEAVE("S"), RETIRE("X");

	private String code;

	IdStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
