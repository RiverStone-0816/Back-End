package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 번호타입
 */
public enum NumberType implements CodeHasable<Byte> {
	HUNT((byte) 0), PERSONAL((byte) 1), SERVICE((byte) 2), CONFERENCE((byte) 3);

	private Byte code;

	NumberType(byte code) {
		this.code = code;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public static NumberType of (byte value) {
		for (NumberType type : NumberType.values()) {
			if (type.code == value)
				return type;
		}

		return null;
	}
}
