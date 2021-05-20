package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 상담톡스케쥴 구분
 * A: 자동멘트요청, G: 상담톡그룹연결, P: 개인상담원연결
 */
public enum TalkScheduleKind implements CodeHasable<String> {
	AUTO_MENT_REQUEST("A"), SERVICE_BY_GROUP_CONNECT("G"), PERSON_CONSULTATION_CONNECT("P");

	private String code;

	TalkScheduleKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
