package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum DisplayType implements CodeHasable<String> {
    TEXT("text"), IMAGE("image"), CARD("card"), LIST("list");

    private final String code;

    DisplayType(String code) {
        this.code = code;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public static DisplayType of(String value) {
        for (DisplayType type : DisplayType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
