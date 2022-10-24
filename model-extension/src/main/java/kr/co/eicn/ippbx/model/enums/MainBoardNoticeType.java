package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MainBoardNoticeType implements CodeHasable<String> {
    TOP("Y"), NORMAL("N");

    private final String code;

    MainBoardNoticeType(String code) { this.code = code;}

    @Override
    public String getCode() {
        return code;
    }
}
