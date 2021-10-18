package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum TalkMemberDistributionType implements CodeHasable<String> {
    NO("NO"), REGULAR("RR"), LEAST_ORDER("LC"), FIRST_DISTRIBUTION("LR"), LEAST_LEFT("CL");

    private final String code;

    TalkMemberDistributionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TalkMemberDistributionType of(String value) {
        for (TalkMemberDistributionType type : TalkMemberDistributionType.values()) {
            if (type.getCode().equals(value))
                return type;
        }
        return null;
    }
}
