package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * FIELD: 담당자아이디 사용, CAMPAIGN: 상담원지정
 */
public enum PrvMemberKind implements CodeHasable<String> {
    FIELD("FIELD"), CAMPAIGN("CAMPAIGN");

    private String code;

    PrvMemberKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
