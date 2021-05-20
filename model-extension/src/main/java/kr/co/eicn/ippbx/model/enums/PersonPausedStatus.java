package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 상담원 상태
 * 0 : 상담대기
 * 1 : 상담중
 * 2 : 후처리
 * 3 : 휴식
 * 4 : 식사
 */
public enum PersonPausedStatus implements CodeHasable<Integer> {
    WAIT_FOR_CONSULTATION(0), DURING_CONSULTATION(1), AFTER_TREATMENT(2), REST(3), MEAL(4);
    private Integer code;

    PersonPausedStatus(Integer code) {
        this.code = code;
    }

    public static PersonPausedStatus of(Integer value) {
        for (PersonPausedStatus status : PersonPausedStatus.values()) {
            if (status.getCode().equals(value))
                return status;
        }

        return null;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
