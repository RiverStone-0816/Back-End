package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * FIELD: 담당자아이디 사용, CAMPAIGN: 상담원지정
 */
public enum PrvMemberKind implements CodeHasable<String> {
    FIELD("FIELD"), CAMPAIGN("CAMPAIGN");

    private final String code;

    PrvMemberKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
