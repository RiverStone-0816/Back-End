package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum LicenseListType implements CodeHasable<String> {
    PDS("PDS"), STAT("STAT"), CTI("CTI"), TALK("TALK"), EMAIL("EMAIL"), CHATT("CHATT"), STT("STT"),
    ASTIN("ASTIN"), ASTOUT("ASTOUT"), ASTSTT("BSTT");

    private final String code;

    LicenseListType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
