package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum WebVoiceAreaType implements CodeHasable<String> {
    INPUT("INPUTAREA"), TEXT("TEXTAREA"), HEADER("HEADERAREA");

    private final String code;

    WebVoiceAreaType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
