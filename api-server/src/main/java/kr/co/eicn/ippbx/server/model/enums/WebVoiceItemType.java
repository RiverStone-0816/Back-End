package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum WebVoiceItemType implements CodeHasable<String> {
    DTMF("DTMF"), INPUT("INPUTDIGIT"), HEADER("HEADER"), TEXT("TEXT");

    private final String code;

    WebVoiceItemType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
