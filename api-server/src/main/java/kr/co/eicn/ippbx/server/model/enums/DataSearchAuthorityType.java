package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

public enum DataSearchAuthorityType implements CodeHasable<String> {
    NONE("NO"), MINE("MY"), BRANCH("BRANCH"), AREA("AREA"), PART("PART"), ALL("ALL");

    private String code;

    DataSearchAuthorityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DataSearchAuthorityType of (String code) {
        for (DataSearchAuthorityType type : DataSearchAuthorityType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
