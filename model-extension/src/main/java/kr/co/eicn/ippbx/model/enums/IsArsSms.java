package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum IsArsSms implements CodeHasable<String> {
    ARS("ARS"), SMS("SMS");

    private String code;

    IsArsSms(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
