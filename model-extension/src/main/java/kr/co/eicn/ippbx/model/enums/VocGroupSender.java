package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum VocGroupSender implements CodeHasable<String> {
    MEMBER("MEMBER"), AUTO("AUTO");

    private final String code;

    VocGroupSender(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
