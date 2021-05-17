package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * pds_group.rid_kind
 * CAMPAIGN:그룹별번호, FIELD:그룹별직접입력
 */
public enum PDSGroupRidKind implements CodeHasable<String> {
	GROUP_BY_RID("CAMPAIGN"), UPLOAD_FIELD("FIELD");
	private String code;

	PDSGroupRidKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
