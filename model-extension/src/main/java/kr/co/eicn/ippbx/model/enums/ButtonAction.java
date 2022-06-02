package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum ButtonAction implements CodeHasable<String> {
    CONNECT_NEXT_BLOCK(""), CONNECT_BLOCK("block"), CONNECT_URL("url"), CONNECT_MEMBER("member"), CONNECT_PHONE("phone"), CONNECT_API("api"),
    CONNECT_FIRST_BLOCK("first"), CONNECT_BEFORE_BLOCK("before"), CONNECT_FORM_BLOCK("form"), AUTH_IPCC("ipcc");

    private final String code;

    ButtonAction(String code) {
        this.code = code;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public static ButtonAction of(String value) {
        for (ButtonAction type : ButtonAction.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
