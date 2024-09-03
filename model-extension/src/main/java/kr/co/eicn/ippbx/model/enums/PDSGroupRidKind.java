package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > RID(발신번호)설정 구분
 * CAMPAIGN:그룹별RID지정
 * FIELD:고객별 RID설정에 따름
 */
public enum PDSGroupRidKind implements CodeHasable<String> {
	CAMPAIGN("CAMPAIGN"),
	FIELD("FIELD");
	private final String code;

	PDSGroupRidKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
