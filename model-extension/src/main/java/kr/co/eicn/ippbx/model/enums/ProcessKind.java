package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * CONTINUE: 비사용할 때까지 진행
 * NOTUSE: 사용안함
 * TERM: 일정기간동안 진행
 * DELETE: 삭제
 */
public enum ProcessKind implements CodeHasable<String> {
    CONTINUE("C"), NOTUSE("N"), TERM("T"), DELETE("D");

    private final String code;

    ProcessKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
