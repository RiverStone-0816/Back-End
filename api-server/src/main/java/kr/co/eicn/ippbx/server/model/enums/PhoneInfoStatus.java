package kr.co.eicn.ippbx.server.model.enums;


import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum PhoneInfoStatus implements CodeHasable<String> {
    REGISTERED("registered"), UNREACHABLE("unreachable"), RESET("reset");

    private final String code;

    PhoneInfoStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
