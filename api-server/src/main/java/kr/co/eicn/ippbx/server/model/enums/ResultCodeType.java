package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 *  이메일 이력 result_code
 */
public enum ResultCodeType implements CodeHasable<String> {
    REGISTER("R"), PROCESSING("P"), TRANSFER("T"), COMPLETE("C");

    private String code;

    ResultCodeType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
