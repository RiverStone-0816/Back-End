package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum AuthDisplayType implements CodeHasable<String> {
    TEXT("text"), NUMBER("number"), SECRET("secret"), TITLE("");

    private final String code;

    @JsonValue
    public String getCode() {
        return code;
    }

    AuthDisplayType(String code) {
        this.code = code;
    }

    public static AuthDisplayType of(String value) {
        for (AuthDisplayType type : AuthDisplayType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
