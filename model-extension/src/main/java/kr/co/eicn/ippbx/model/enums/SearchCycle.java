package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum SearchCycle implements CodeHasable<String> {
    DATE("date"), HOUR("hour"), WEEK("week"), MONTH("month"), DAY_OF_WEEK("dow");

    private final String code;

    SearchCycle(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
