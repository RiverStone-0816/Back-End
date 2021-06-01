package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * pds_group.connect_kind 연결대상 구분
 * MEMBER: 상담원그룹, PDS_IVR: PDS_IVR연결, ARS_RSCH:ARS설문
 */
public enum PDSGroupConnectKind implements CodeHasable<String> {
	CONSULTATION_GROUP("MEMBER"), PDS_IVR("PDS_IVR"), ARS_RESEARCH("ARS_RSCH"), RESEARCH("RSCH");

	private final String code;

	PDSGroupConnectKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
