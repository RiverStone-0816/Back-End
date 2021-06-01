package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * common_type kind
 *  MAINDB:고객DB유형, CON:연동DB유형, RS:상담결과유형
 * PRV: 프리뷰유형, PDS:PDS유형, CUSTOM_TALK:TALK유형
 */
public enum CommonTypeKind implements CodeHasable<String> {
	MAIN_DB("MAINDB"), CONSULTATION_RESULTS("RS"), PREVIEW("PRV"), PDS("PDS"), LINK_DB("CON"), CUSTOM_TALK("TALK");

	private final String code;

	CommonTypeKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
