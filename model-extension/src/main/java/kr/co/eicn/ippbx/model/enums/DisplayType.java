package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum DisplayType implements CodeHasable<String> {
    TEXT("text"), IMAGE("image"), CARD("card"), LIST("list");

    private final String code;

    DisplayType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
