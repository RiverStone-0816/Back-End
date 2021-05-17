package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * PBX: 내선별, DIRECT: 직접입력, NUMBER: 번호선택
 */
public enum BillingKind implements CodeHasable<String> {
    PBX("PBX"), DIRECT("DIRECT"), NUMBER("NUMBER");

    private String code;

    BillingKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
