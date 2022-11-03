package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MainBoardPopupTarget implements CodeHasable<String> {
    ADMIN("A"), CUSTOMER("B"), ALL("C");

    private final String code;

    MainBoardPopupTarget(String code) { this.code = code; }

    @Override
    public String getCode() {
        return code;
    }
}
