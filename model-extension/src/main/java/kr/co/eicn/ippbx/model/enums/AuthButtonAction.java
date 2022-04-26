package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum AuthButtonAction implements CodeHasable<String> {
    CONNECT_FIRST("first"), CONNECT_BEFORE("before"), CONNECT_API("api");

    private final String code;

    @JsonValue
    public String getCode() {
        return code;
    }

    AuthButtonAction(String code) {
        this.code = code;
    }

    public static AuthButtonAction of(String value) {
        for (AuthButtonAction type : AuthButtonAction.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
