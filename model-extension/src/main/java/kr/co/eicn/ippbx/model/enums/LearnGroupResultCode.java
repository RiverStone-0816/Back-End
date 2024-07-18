package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

public enum LearnGroupResultCode implements CodeHasable<String> {
    WAIT("A"), ING("B"), FAIL("C"), SUCCESS("D");

    private final String code;

    LearnGroupResultCode(String code) {this.code = code;}

    @JsonValue
    public String getCode() {return code;}
}
