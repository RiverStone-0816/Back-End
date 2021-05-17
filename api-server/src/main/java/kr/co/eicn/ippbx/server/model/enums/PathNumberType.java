package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum PathNumberType implements CodeHasable<String> {
    INI("INI"), SECOND("SECOND");

    private String code;

    PathNumberType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
