package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum BoardType implements CodeHasable<String> {
    NOTICE("N"), MANUAL("M");
    private String code;

    BoardType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
