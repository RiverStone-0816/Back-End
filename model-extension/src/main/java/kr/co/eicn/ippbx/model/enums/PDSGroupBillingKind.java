package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 과금번호설정 구분
 *  pds_group.billing_kind
 *  PBX:내선별PBX설정, NUMBER:그룹별번호, DIRECT:그룹별직접입력
 */
public enum PDSGroupBillingKind implements CodeHasable<String> {
	PBX("PBX"), GROUP_BY_NUMBER("NUMBER"), GROUP_BY_DIRECT("DIRECT");

	private final String code;

	PDSGroupBillingKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
