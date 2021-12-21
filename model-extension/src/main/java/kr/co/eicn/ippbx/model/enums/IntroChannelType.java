package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum IntroChannelType implements CodeHasable<String> {
    KAKAO("kakao", "/resources/images/kakao-icon.png"),
    EICN("eicn", "/resources/images/chatbot-icon.svg"),
    LINE("naverline", "/resources/images/line-icon.png"),
    NAVER("navertt", "/resources/images/ntalk-icon.png");

    private final String code;
    private final String imagePath;

    IntroChannelType(String code, String imagePath) {
        this.code = code;
        this.imagePath = imagePath;
    }

    public String getCode() {
        return code;
    }

    public String getImagePath() {
        return imagePath;
    }

    public static IntroChannelType of(String value) {
        for (IntroChannelType introChannelType : IntroChannelType.values()) {
            if (Objects.equals(introChannelType.code, value))
                return introChannelType;
        }
        return null;
    }
}
