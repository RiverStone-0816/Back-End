package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 *  스케쥴유형별 입력 데이터
 */
public enum ScheduleKindData implements CodeHasable<Integer> {
	NONE_WORKING_MENT(1), EMERGENCY_MENT(2), WORKING_MENT(4), LUNCH_MENT(5), FIRST_GREETING_MENT(6);

	private Integer code;

	ScheduleKindData(int code) {
		this.code = code;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}
}
