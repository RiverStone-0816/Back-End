package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 상담톡스케쥴 구분
 * A: 자동멘트전송, G: 서비스별그룹연결, B: 웹챗시나리오
 */
public enum TalkScheduleKind implements CodeHasable<String> {
	AUTO_MENT_REQUEST("A"), SERVICE_BY_GROUP_CONNECT("G"), CHAT_BOT_CONNECT("B");

	private final String code;

	TalkScheduleKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
