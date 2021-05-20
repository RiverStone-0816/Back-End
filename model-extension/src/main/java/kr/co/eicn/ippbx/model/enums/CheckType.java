package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * INNER : 내선
 * CALL_BACK : 콜백
 * WORK_TIME : 업무시간
 **/
public enum CheckType implements CodeHasable<String> {
    INNER("inner"), CALL_BACK("cb"), WORK_TIME("time");

    private final String code;

    CheckType(String code) {
        this.code = code;
    }

    public String getCode() { return code; }
}
