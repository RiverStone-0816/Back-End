package kr.co.eicn.ippbx.server.model.enums;
import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

public enum RecordType implements CodeHasable<String> {
    RECORDING("M"), NONE_RECORDING("S");

    private String code;

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
