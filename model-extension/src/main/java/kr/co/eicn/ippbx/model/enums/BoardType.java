package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum BoardType implements CodeHasable<String> {
    NOTICE("N"), MANUAL("M");
    private final String code;

    BoardType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
