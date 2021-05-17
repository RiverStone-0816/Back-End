package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 일괄녹취다운관리 다운로드 요청상태?
 */
public enum RecordDownStatusKind implements CodeHasable<String> {
	REQUEST_COMPLETED("A"), IN_READY("B"), COMPLETE("C");

	private String code;

	RecordDownStatusKind(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
