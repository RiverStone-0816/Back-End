package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum SendCategoryType implements CodeHasable<String> {
    SMS("S"), FAX("F"), EMAIL("E");

    private String code;

    SendCategoryType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
