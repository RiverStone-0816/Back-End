package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 블랜딩 모드
 * N : 사용하지 않음
 * W : 고객대기자 기준
 * T : 일정시간 기준
 * */
public enum BlendingMode implements CodeHasable<String> {
	NOT_USE("N"), WAIT_CUSTOMER("W"), TIME("T");

	private final String code;

	BlendingMode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}