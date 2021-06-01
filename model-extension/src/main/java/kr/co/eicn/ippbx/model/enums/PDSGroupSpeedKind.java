package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS속도 기준
 *  pds_group.speed_kind
 *  MEMBER:대기중상담원기준, CHANNEL:동시통화기준
 */
public enum PDSGroupSpeedKind implements CodeHasable<String> {
	STANDBY_CONSULTATION_BASE("MEMBER"), CONCURRENT_CALL_BASE("CHANNEL");

	private final String code;

	PDSGroupSpeedKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
