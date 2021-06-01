package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum DayOfWeek implements CodeHasable<String> {
    MONDAY("Mon"), TUESDAY("Tue"), WEDNESDAY("Wed"), THURSDAY("Thu"), FRIDAY("Fri"), SATURDAY("Sat"), SUNDAY("Sun");

    private final String code;

    DayOfWeek(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
