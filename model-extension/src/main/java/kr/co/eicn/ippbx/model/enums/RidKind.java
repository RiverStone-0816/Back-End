package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PBX: 내선별, CAMPAIGN: 그룹별 RID 지정
 */
/**
 * 프리뷰그룹 > RID(발신번호)설정 구분
 * CAMPAIGN:내선별 PBX 설정에 따름
 * PBX:그룹별번호
 */
public enum RidKind implements CodeHasable<String> {
    CAMPAIGN("CAMPAIGN"), PBX("PBX");

    private final String code;

    RidKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
