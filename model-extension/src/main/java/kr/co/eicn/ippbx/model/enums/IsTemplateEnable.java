package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum IsTemplateEnable implements CodeHasable<String> {
    ENABLE("Y"), DISABLE("N");

    private final String code;

    IsTemplateEnable(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
