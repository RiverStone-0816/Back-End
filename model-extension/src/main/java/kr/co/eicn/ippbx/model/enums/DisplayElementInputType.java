package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum DisplayElementInputType implements CodeHasable<String> {
    NUMBER("number"), TEXT("text"), CALENDAR("calendar"), TINE("time"), SECRET("secret");

    private final String code;

    DisplayElementInputType(String code) {
        this.code = code;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public static DisplayElementInputType of(String value) {
        for (DisplayElementInputType type : DisplayElementInputType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
