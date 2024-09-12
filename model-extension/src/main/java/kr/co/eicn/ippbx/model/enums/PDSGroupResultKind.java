package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PDS 그룹 > 상담결과화면 구분
 * RS:상담결과화면,
 * NONE:없음 또는 고객사 화면
 */
public enum PDSGroupResultKind implements CodeHasable<String> {
    RS("RS"), NONE("");
    private final String code;

    PDSGroupResultKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
