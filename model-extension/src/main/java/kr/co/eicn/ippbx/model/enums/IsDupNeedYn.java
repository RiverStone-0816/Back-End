package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum IsDupNeedYn implements CodeHasable<String> {
    USE("Y"), SKIP("N");

    private String code;

    IsDupNeedYn(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
