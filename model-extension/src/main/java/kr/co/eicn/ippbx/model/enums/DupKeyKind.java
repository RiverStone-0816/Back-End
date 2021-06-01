package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 * FLD_NUM: 전화번호+필수항목중선택
 * FLD: 필수항목중선택
 * NUM: 전화번호
 */
public enum DupKeyKind implements CodeHasable<String> {
    FIELD_NUMBER("FLD_NUM"), FIELD("FLD"), NUMBER("NUM");

    private final String code;

    DupKeyKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static DupKeyKind of (String code) {
        for (DupKeyKind type : DupKeyKind.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
