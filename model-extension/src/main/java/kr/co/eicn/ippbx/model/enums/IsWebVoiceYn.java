package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum IsWebVoiceYn implements CodeHasable<String> {
    WEB_VOICE_N("N"), WEB_VOICE_Y("Y");

    private final String code;

    IsWebVoiceYn(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static IsWebVoiceYn of (String code) {
        for (IsWebVoiceYn type : IsWebVoiceYn.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
