package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum DaemonType implements CodeHasable<String> {
    SYSTEM("SYS"), NODEJS("nodejs"), JAVA("java");

    private final String code;

    DaemonType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
