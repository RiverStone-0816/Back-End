package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum SlidingYn implements CodeHasable<String> {
    SLIDING_Y("Y"), SLIDING_N("N");

    private String code;

    SlidingYn(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
