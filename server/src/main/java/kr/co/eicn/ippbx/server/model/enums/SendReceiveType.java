package kr.co.eicn.ippbx.server.model.enums;

/**
 * 송수신유형
 * SEND : 송신, RECEIVE : 수신, AUTO_FIRST : 자동첫인사, AL : 비수신개수초과
 * AUTO_SCHEDULE : 스케쥴링멘트, END : 대화방내림(종료), CUSTOM_UPDATE : 고객정보변경, SZ : 상담원이 상담건 찜함함
 * */
public enum SendReceiveType {
    SEND("S"), RECEIVE("R"), AUTO_FIRST("AF"), AL("AL"),
    AUTO_SCHEDULE("AS"), END("SE"), CUSTOM_UPDATE("SCU"), SZ("SZ") ;

    private final String code;

    SendReceiveType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
