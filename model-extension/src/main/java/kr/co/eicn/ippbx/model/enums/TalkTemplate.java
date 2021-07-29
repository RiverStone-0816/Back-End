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

	public static TalkTemplate of (String value) {
		for (TalkTemplate type : TalkTemplate.values()){
			if (type.getCode().equals(value))
				return type;
		}

		return null;
	}
}
