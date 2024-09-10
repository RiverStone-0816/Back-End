package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > 과금번호설정 구분
 * PBX:내선별 PBX 설정에 따름
 * NUMBER:그룹별번호
 * DIRECT:그룹별직접입력
 */
public enum PDSGroupBillingKind implements CodeHasable<String> {
    PBX("PBX"),
    NUMBER("NUMBER"),
    DIRECT("DIRECT");

    private final String code;

    PDSGroupBillingKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
