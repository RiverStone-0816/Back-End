package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 * BLACK, VIP ROUTE 유형(type)
 */
public enum RouteType implements CodeHasable<String> {
    RANK("A"), HUNT("B");

    private final String code; //유형

    RouteType(String code) {
        this.code = code;
    }

    public static RouteType of(String value) {
        for (RouteType type : RouteType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
