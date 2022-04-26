package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

//EMAIL("EMAIL"),
public enum MultichannelChannelType implements CodeHasable<String> {
    PHONE("PHONE"),  EMAIL("EMAIL"), TALK("TALK"), ECHBT("ECHBT");

    private final String code;

    MultichannelChannelType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
