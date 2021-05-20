package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 암호환된 연결방식
 */
public enum SendAuthConnType implements CodeHasable<String> {
    TLS("T"), SSL("S");

    private String code;

    SendAuthConnType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
