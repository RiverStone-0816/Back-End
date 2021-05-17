package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 비상시 포워딩 타입
 */
public enum ForwardingType implements CodeHasable<String> {
    NONE("N"), INNER("I"), OUTER("O");

    private String code;

    ForwardingType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
