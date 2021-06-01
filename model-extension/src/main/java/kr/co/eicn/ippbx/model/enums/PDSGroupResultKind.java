package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * pds_group.result_kind 상담결과구분
 * RS:상담결과유형화면, RSCH:설문화면
 */
public enum PDSGroupResultKind implements CodeHasable<String> {
	CONSULTATION_RESULTS("RS"), RSCH("RSCH");
	private final String code;

	PDSGroupResultKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
