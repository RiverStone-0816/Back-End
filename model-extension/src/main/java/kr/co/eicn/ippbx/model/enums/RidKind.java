package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PBX: 내선별, CAMPAIGN: 그룹별 RID 지정
 */
public enum RidKind implements CodeHasable<String> {
    PBX("PBX"), CAMPAIGN("CAMPAIGN");

    private String code;

    RidKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
