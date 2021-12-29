package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum WebVoiceItemType implements CodeHasable<String> {
    DTMF("DTMF"), INPUT("INPUTDIGIT"), HEADER("HEADER"), TEXT("TEXT"), BUTTON("BUTTON");

    private final String code;

    WebVoiceItemType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
