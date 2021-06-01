package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PBX: 내선별, DIRECT: 직접입력, NUMBER: 번호선택
 */
public enum BillingKind implements CodeHasable<String> {
    PBX("PBX"), DIRECT("DIRECT"), NUMBER("NUMBER");

    private final String code;

    BillingKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
