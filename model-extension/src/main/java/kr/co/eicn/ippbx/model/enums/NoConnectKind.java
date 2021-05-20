package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum NoConnectKind implements CodeHasable<String> {
    CONTEXT("CONTEXT"), HUNT("RESERVE_HUNT"), NONE("");

    private final String code;

    public String getCode() {
        return code;
    }

    NoConnectKind(String code) {
        this.code = code;
    }

    public static NoConnectKind of(String value) {
        for (NoConnectKind type : NoConnectKind.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
