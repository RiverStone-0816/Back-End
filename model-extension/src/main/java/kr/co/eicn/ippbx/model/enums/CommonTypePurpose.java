package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 아웃바운드 유형관리 용도구분
 * PCC: PDS[상담원 연결], PIC:PDS[IVR연결], SVY:설문조사, VDU:VOC[DB업로드], VCS:VOC[상담화면], ADU:ACS[DB업로드], ACS:ACS[상담화면]
 */
public enum CommonTypePurpose implements CodeHasable<String> {
	PDS_CONSULTATION_CONN("PCC"), PDS_IVR_CONN("PIC"), SURVEY("SVY"), VOC_DB_UPLOAD("VDU"), VOC_CONSULTATION_SCREEN("VCS"),
	ACS_DB_UPLOAD("ADU"), ACS_CONSULTATION_SCREEN("ACS");

	private String code;

	CommonTypePurpose(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
