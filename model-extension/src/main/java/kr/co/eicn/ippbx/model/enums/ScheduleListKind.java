package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

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
