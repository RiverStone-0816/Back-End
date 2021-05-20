package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 템플릿 타입.
 */
public enum TalkTemplate implements CodeHasable<String> {
	PERSON("P"), GROUP("G"), COMPANY("C");

	private final String type;

	TalkTemplate(String code) {
		this.type = code;
	}

	@Override
	public String getCode() {
		return this.type;
	}
}
