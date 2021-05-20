package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 070번호 상태
 * (0:비사용, 1사용)
 */
public enum Number070Status implements CodeHasable<Byte> {
	NON_USE((byte)0), USE((byte)1);

	private Byte code;

	Number070Status(Byte code) {
		this.code = code;
	}

	@Override
	public Byte getCode() {
		return this.code;
	}
}
