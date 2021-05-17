package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum ProtectArs implements CodeHasable<String> {
    S1("sexual_1"), S2("sexual_2"), L1("violence_1"), L2("violence_2");
    private String code;

    ProtectArs(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

