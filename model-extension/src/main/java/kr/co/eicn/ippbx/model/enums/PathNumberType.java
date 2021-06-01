package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum PathNumberType implements CodeHasable<String> {
    INI("INI"), SECOND("SECOND");

    private final String code;

    PathNumberType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
