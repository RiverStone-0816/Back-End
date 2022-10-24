package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MainBoardTargetCompany implements CodeHasable<String> {
    TOTAL("A"), COMPANY("T");

    private final String code;

    MainBoardTargetCompany(String code) { this.code = code;}

    @Override
    public String getCode() {
        return code;
    }
}
