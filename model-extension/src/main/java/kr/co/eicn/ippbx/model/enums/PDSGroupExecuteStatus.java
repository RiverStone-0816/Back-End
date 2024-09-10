package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > 실행상태 구분
 * UN_EXECUTED:실행안됨
 * PREPARING:준비중
 * READY:준비완료
 * PROCEEDING:진행중
 * STOP:정지
 * COMPLETE:완료됨
 * FINISHED:마침
 * EXCLUDED:PDS그룹삭제됨
 */
public enum PDSGroupExecuteStatus implements CodeHasable<String> {
    UN_EXECUTED(""),
    PREPARING("I"),
    READY("R"),
    PROCEEDING("P"),
    STOP("S"),
    COMPLETE("C"),
    FINISHED("D"),
    EXCLUDED("X");

    private final String code;

    PDSGroupExecuteStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
