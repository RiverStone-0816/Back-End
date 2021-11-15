package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum FallbackAction implements CodeHasable<String> {
    FIRST("first"), CONNECT_MEMBER("member"), CONNECT_URL("url"), CONNECT_BLOCK("block"), CONNECT_PHONE("phone");

    private final String code;

    FallbackAction(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
