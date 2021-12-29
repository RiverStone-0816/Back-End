package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum WebVoiceItemYn implements CodeHasable<String> {
    USE("Y"), UNUSED("N");

    private final String code;

    WebVoiceItemYn(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
