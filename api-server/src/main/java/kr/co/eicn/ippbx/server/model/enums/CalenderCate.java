package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

public enum CalenderCate implements CodeHasable<String>{
    /**
     * MY_CAL = 내일정
     * HOLIDAY = 휴일
     * COMPANY_CAL = 회사일정
     */
    MY_CAL("0"),
    HOLIDAY("1"),
    COMPANY_CAL("2");

    private String code;

    CalenderCate(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static CalenderCate of(String value) {
        for (CalenderCate e : values()) {
            if (Objects.equals(e.getCode(), value))
                return e;
        }
        return null;
    }
}
