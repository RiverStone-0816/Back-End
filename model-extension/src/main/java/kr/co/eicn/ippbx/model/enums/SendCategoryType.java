package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

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
