package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum SttType implements CodeHasable<String> {
    STT("Y"), NONE_STT("N");

    private final String code;

    SttType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static SttType of (String code) {
        for (SttType type : SttType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
