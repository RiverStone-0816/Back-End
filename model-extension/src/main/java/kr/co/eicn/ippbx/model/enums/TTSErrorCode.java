package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum TTSErrorCode implements CodeHasable<Integer> {
	ILLEGAL_PARAMETER(-1), UN_KNOWN_HOST(-2), NO_DATA(-3), BAD_PROTOCOL(-4), EXCEPTION5(-5), EXCEPTION6(-6);

	private final Integer code;

	TTSErrorCode(int code) {
		this.code = code;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}
}
