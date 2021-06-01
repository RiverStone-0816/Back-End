package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum MonitorQueueType implements CodeHasable<String> {
    SUMMARY("SU"), STATUS("ST");

    private final String code;

    MonitorQueueType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
