package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

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