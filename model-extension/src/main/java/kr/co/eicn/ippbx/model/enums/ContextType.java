package kr.co.eicn.ippbx.model.enums;

/**
 * INBOUND : 인바운드 통화
 * INBOUND_INNER : 인바운드 내선통화
 * OUTBOUND : 아웃바운드 통화
 * OUTBOUND_INNER : 아웃바운드 내선통화
 * CALL_BACK : 콜백 남길시
 * DIRECT_CALL : 직통전화
 * HUNT_CALL : 큐연결
 * CLICK_DIAL : ??
 * ARS_AUTH : 삼담원 로그인시 이중인증 콜
 * */
public enum ContextType {
    INBOUND("inbound"), INBOUND_INNER("inbound_inner"), OUTBOUND("outbound"), OUTBOUND_INNER("outbound_inner"),
    CALL_BACK("busy_context"), DIRECT_CALL("pers_context"), HUNT_CALL("hunt_context"),  CLICK_DIAL("CLICK_DIAL"),
    ARS_AUTH("ars_auth_context");

    private final String code;

    ContextType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
