package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

public enum TranscribeDataResultCode implements CodeHasable<String> {
    A("A"), B("B"), NO("NO"), OK("OK");

    private String code;

    TranscribeDataResultCode(String code) {this.code = code;}

    @JsonValue
    public String getCode() {return code;}
}
