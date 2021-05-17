package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 속도 배수
 */
public enum PDSGroupSpeedMultiple implements CodeHasable<Integer> {
	ONE(10), ONE_POINT_FIVE(15), TWO(20), TWO_POINT_FIVE(25), THREE(30), THREE_POINT_FIVE(35),
	FOUR(40), FOUR_POINT_FIVE(45), FIVE(50), FIVE_POINT_FIVE(55);
	private Integer code;

	PDSGroupSpeedMultiple(int code) {
		this.code = code;
	}

	@Override
	public Integer getCode() {
		return code;
	}
}
