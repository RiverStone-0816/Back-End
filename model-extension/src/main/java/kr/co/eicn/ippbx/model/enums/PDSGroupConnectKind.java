package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * pds_group.connect_kind 연결대상 구분
 * PDS_IVR : IVR
 * ARS_RSCH : ARS설문
 * MEMBER : 상담원그룹
 */
public enum PDSGroupConnectKind implements CodeHasable<String> {
	PDS_IVR("PDS_IVR"),
	MEMBER("MEMBER"),
	ARS_RSCH("ARS_RSCH");

	private final String code;

	PDSGroupConnectKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
