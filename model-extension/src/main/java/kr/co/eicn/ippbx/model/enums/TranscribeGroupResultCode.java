package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

public enum TranscribeGroupResultCode implements CodeHasable<String> {
    WAIT("A"), ING("ING"), REQ("B"), FAIL("C"), SUCCESS("D");

    private final String code;

    TranscribeGroupResultCode(String code) {this.code = code;}

    @JsonValue
    public String getCode() {return code;}
}
