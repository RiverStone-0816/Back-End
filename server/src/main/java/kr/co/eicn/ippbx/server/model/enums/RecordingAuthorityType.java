package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

/**
 * 녹취권한 타입
 */
public enum RecordingAuthorityType implements CodeHasable<String> {
    NONE("NO"), MINE("MY"), GROUP("GROUP"), ALL("ALL");

    private String code;

    RecordingAuthorityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static RecordingAuthorityType of (String code) {
        for (RecordingAuthorityType type : RecordingAuthorityType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
