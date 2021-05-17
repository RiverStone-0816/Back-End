package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum MultichannelChannelType implements CodeHasable<String> {
    PHONE("PHONE"), EMAIL("EMAIL"), TALK("TALK");

    private final String code;

    MultichannelChannelType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
