package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum ButtonAction implements CodeHasable<String> {
    CONNECT_NEXT_BLOCK(""), CONNECT_BLOCK("block"), CONNECT_URL("url"), CONNECT_MEMBER("member"), CONNECT_PHONE("phone"), CONNECT_API("api");

    private final String code;

    ButtonAction(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
