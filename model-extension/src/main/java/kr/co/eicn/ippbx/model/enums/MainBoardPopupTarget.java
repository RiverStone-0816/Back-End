package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MainBoardPopupTarget implements CodeHasable<String> {
    ALL("A"), ADMIN("B"), CUSTOMER("C");

    private final String code;

    MainBoardPopupTarget(String code) { this.code = code; }

    @Override
    public String getCode() {
        return code;
    }
}
