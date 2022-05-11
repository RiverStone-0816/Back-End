package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum SoftPhoneType implements CodeHasable<String> {
    SOFTPHONE("Y"), NONE_SOFTPHONE("N");

    private final String code;

    SoftPhoneType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static SoftPhoneType of (String code) {
        for (SoftPhoneType type : SoftPhoneType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
