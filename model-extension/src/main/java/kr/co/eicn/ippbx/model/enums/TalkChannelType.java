package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum TalkChannelType implements CodeHasable<String> {
    EICN("eicn"),KAKAO("kakao");

    private final String code;

    TalkChannelType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
