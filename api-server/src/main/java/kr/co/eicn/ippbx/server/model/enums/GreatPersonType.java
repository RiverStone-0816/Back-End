package kr.co.eicn.ippbx.server.model.enums;

/**
 * 우수상담원 구분
 */
public enum GreatPersonType {
    MAXIMUM_RECEIVE_COUNT(""), MAXIMUM_SEND_COUNT(""), LONGEST_RECEIVE_TIME(""),
    LONGEST_SEND_TIME(""), LONGEST_CALL_TIME(""), LONGEST_CALLBACK_COUNT("");

    private String code;

    GreatPersonType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
