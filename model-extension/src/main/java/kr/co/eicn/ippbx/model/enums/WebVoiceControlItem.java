package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum WebVoiceControlItem implements CodeHasable<String> {
    PREV("B"), FIRST("F"), COUNSELING("C"), END("H");

    private final String code;

    WebVoiceControlItem(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
