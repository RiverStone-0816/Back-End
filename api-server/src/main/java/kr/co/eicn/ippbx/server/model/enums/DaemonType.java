package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

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
