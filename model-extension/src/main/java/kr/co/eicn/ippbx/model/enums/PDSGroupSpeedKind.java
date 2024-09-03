package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > 속도 기준
 * MEMBER:대기중상담원기준
 * CHANNEL:동시통화기준
 */
public enum PDSGroupSpeedKind implements CodeHasable<String> {
    MEMBER("MEMBER"),
    CHANNEL("CHANNEL");

    private final String code;

    PDSGroupSpeedKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
