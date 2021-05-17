package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum Button implements CodeHasable<String> {
	  DIAL_0("0", 0)
	, DIAL_1("1", 1)
	, DIAL_2("2", 2)
	, DIAL_3("3", 3)
	, DIAL_4("4", 4)
	, DIAL_5("5", 5)
	, DIAL_6("6", 6)
	, DIAL_7("7", 7)
	, DIAL_8("8", 8)
	, DIAL_9("9", 9)
	, DIAL_PRE_STAGE("*", 10)
	, DIAL_LISTEN_AGAIN("#", 11)
	, X("X", 12);

	private String code;
	private int intValue;

	Button(String code, int intValue) {
		this.code = code;
		this.intValue = intValue;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	public int getIntValue() {
		return intValue;
	}

	public static Button of(String value) {
		for (Button button : Button.values()) {
			if (button.getCode().equals(value))
				return button;
		}
		return null;
	}
}
