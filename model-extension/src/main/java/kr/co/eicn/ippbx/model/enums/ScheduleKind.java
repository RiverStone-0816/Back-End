package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * schedule_group_list kind 스케쥴 유형
 * S:음원만플레이
 * U:번호직접연결(내부번호연결)
 * F:착신전환(외부번호연결)
 * I:IVR연결
 * C:예외컨텍스트
 * V:음성사서함
 * CI:예외처리후번호연결
 * CD:예외처리후IVR
 * SMS:문자발송
 */
public enum ScheduleKind implements CodeHasable<String> {
    ONY_SOUND_PLAY("S"), DIRECT_NUMBER("D"),
    CALL_FORWARDING("F"), IVR_CONNECT("I"),
    EXCEPTION_CONTEXT("C"), VOICE("V"),
    EXCEPTION_CONTEXT_IVR_CONNECT("CI"),
    EXCEPTION_CONTEXT_DIRECT_NUMBER("CD"),
    SMS("SMS");

    private final String code;

    ScheduleKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
