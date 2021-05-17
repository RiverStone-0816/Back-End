package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * MEMBER:일정상담사들의 콜만 진행
 * CIDNUM: 특정발신번호 CID만 진행 (OUTBOUND만 해당)
 * SVCNUM: 수신대표번호만 진행 (INBOUND만 해당)
 * ALL: 모든 발신
 * NO: 모두 진행안함
 */
public enum InOutTarget implements CodeHasable<String> {
    MEMBER("MEMBER"), CIDNUM("CIDNUM"), SVCNUM("SVCNUM"), ALL("ALL"), NO("NO");

    private String code;

    InOutTarget(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
