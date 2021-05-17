package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum TalkMode implements CodeHasable<String> {
    MY("MY"), END("END"), TOT("TOT"), OTH("OTH");
    private final String code;

    TalkMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
