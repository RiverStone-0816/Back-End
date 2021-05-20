package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum SendSortType implements CodeHasable<String> {
    DIRECT("D"), CATEGORY("C");

    private String code;

    SendSortType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
