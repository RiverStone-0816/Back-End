package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum MonitorQueueType implements CodeHasable<String> {
    SUMMARY("SU"), STATUS("ST");

    private String code;

    MonitorQueueType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
