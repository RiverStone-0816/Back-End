package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 스케쥴유형구분
 */
public enum ScheduleListKind implements CodeHasable<String> {
	AUTO_MENT_REQUEST("A"), BY_SERVICE_GROUP_REQUEST("G");

	private String code;

	ScheduleListKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
