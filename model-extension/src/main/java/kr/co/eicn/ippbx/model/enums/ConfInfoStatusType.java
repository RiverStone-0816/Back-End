package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 회의 상태
 */
public enum ConfInfoStatusType implements CodeHasable<String> {
    //회의 상태(A:미진행(회의시작전...), B:진행중(회의진행중...), C:진행완료, P:예약날짜만료)
    //회의진행 메시지 동일
    미진행("A"), 진행중("B"), 진행완료("C"), 예약날짜만료("P");

    private String code;

    ConfInfoStatusType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
