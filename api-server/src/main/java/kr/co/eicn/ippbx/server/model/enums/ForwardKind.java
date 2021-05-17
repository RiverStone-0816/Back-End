package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

/**
 * 포워딩 구분
 */
public enum ForwardKind implements CodeHasable<String> {
	INNER("E"), HUNT("H"), REPRESENTATIVE("R"), OUTER("O");

	private String code;
	ForwardKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}

	public static ForwardKind of (String code) {
		for (ForwardKind type : ForwardKind.values()) {
			if (Objects.equals(type.getCode(), code))
				return type;
		}

		return null;
	}
}
