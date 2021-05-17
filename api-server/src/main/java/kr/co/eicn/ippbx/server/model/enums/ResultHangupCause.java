package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum ResultHangupCause implements CodeHasable<String> {
    NORMAL_CALL("16"),
    CALLING("17"),
    NOT_RECEIVE("19"),
    REJECT("21"),
    REJECT2("38") /*TODO: ?*/,
    INVALID_NUMBER("1"),
    NOT_CONNECT_TO_USER("220"),
    MACHINE("221");

    private final String code;

    ResultHangupCause(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}