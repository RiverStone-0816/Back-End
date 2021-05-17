package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

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
