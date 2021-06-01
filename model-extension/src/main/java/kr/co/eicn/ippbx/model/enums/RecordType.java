package kr.co.eicn.ippbx.model.enums;
import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum RecordType implements CodeHasable<String> {
    RECORDING("M"), NONE_RECORDING("S");

    private final String code;

    RecordType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static RecordType of (String code) {
        for (RecordType type : RecordType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
