package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 *  스케쥴러 구분(WEEK:주간, DAY:일별)
 */
public enum ScheduleType implements CodeHasable<String> {
	WEEK("W"), DAY("H");

	private String code;

	ScheduleType(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
