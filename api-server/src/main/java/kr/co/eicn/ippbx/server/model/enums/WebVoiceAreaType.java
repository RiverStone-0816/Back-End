package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

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
