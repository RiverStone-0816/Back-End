package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MainBoardPopupPoint implements CodeHasable<String> {
    AFTER("A"), BEFORE("B");

    private final String code;

    MainBoardPopupPoint(String code) { this.code = code; }

    @Override
    public String getCode() {
        return code;
    }
}
