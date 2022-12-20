package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum ProtectArs implements CodeHasable<String> {
    S1("sexual"), L1("violence");
    private final String code;

    ProtectArs(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

