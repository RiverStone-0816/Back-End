package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

public enum TalkChannelType implements CodeHasable<String> {
    EICN("eicn"),KAKAO("kakao");

    private final String code;

    TalkChannelType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public static TalkChannelType of(String value) {
        for (TalkChannelType code : TalkChannelType.values()){
            if (code.getCode().equals(value))
                return code;
        }

        return null;
    }
}
