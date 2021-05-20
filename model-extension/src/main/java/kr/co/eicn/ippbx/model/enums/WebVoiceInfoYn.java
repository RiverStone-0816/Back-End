package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum WebVoiceInfoYn implements CodeHasable<String> {
    USE("Y"), UNUSED("N");

    private final String code;

    WebVoiceInfoYn(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
