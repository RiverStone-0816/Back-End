package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum TalkStrategy implements CodeHasable<String> {
    NO("NO"),RR("RR"),LC("LC"),LR("LR"),CL("CL");

    private final String code;
    TalkStrategy(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
