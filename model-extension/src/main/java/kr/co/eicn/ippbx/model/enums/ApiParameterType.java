package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum ApiParameterType implements CodeHasable<String> {
    NUMBER("number"), TEXT("text"), CALENDAR("calendar"), TIME("time");

    private final String code;

    ApiParameterType(String code) {
        this.code = code;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public static ApiParameterType of(String value) {
        for (ApiParameterType type : ApiParameterType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
