package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum SlidingYn implements CodeHasable<String> {
    SLIDING_Y("Y"), SLIDING_N("N");

    private final String code;

    SlidingYn(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
