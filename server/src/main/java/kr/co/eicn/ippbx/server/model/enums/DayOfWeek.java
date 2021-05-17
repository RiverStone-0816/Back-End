package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum DayOfWeek implements CodeHasable<String> {
    MONDAY("Mon"), TUESDAY("Tue"), WEDNESDAY("Wed"), THURSDAY("Thu"), FRIDAY("Fri"), SATURDAY("Sat"), SUNDAY("Sun");

    private String code;

    DayOfWeek(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
