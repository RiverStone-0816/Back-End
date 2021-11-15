package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum ApiParameterType implements CodeHasable<String> {
    NUMBER("number"), TEXT("text"), CALENDAR("calendar"), TIME("time");

    private final String code;

    ApiParameterType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
