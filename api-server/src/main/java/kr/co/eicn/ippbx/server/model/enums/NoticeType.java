package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum NoticeType implements CodeHasable<String> {
    NOTICE("Y"), CANCEL("N");

    private String code;

    NoticeType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
