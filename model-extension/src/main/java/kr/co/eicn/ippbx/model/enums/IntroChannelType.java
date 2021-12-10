package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum IntroChannelType implements CodeHasable<String> {
    KAKAO("kakao"), EICN("eicn"), LINE("naverline"), NAVER("navertt");

    private final String code;

    IntroChannelType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static IntroChannelType of(String value) {
        for (IntroChannelType introChannelType : IntroChannelType.values()) {
            if (Objects.equals(introChannelType.code, value))
                return introChannelType;
        }
        return null;
    }
}
