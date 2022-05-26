package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

public enum BlockType implements CodeHasable<String> {
    BLOCK("BL"), FORM("FORM");

    private final String code;

    BlockType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BlockType of(String value) {
        for (BlockType type : BlockType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
