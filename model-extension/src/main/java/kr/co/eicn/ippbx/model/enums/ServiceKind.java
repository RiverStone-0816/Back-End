package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 서비스 구분
 */
public enum ServiceKind implements CodeHasable<String> {
    SC("SC"), CC("CC");

    private final String code;

    ServiceKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
