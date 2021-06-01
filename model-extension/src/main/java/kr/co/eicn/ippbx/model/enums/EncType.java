package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 * 암호화 방식구분
 */
public enum EncType implements CodeHasable<String> {
	NONE("N"), AES_256("B"), ZIP("Y");

	private final String code;

	EncType(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	public static EncType of (String code) {
		for (EncType type : EncType.values()) {
			if (Objects.equals(type.getCode(), code))
				return type;
		}

		return null;
	}
}
