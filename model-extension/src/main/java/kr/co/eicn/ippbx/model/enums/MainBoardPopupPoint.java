package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MainBoardPopupPoint implements CodeHasable<String> {
    AFTER("B"), BEFORE("A");

    private final String code;

    MainBoardPopupPoint(String code) { this.code = code; }

    @Override
    public String getCode() {
        return code;
    }
}
