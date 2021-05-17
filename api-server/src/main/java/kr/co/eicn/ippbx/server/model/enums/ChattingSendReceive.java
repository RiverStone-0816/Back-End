package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum ChattingSendReceive implements CodeHasable<String> {
    SEND("S"), RECEIVE("R"), SELF("SR");

    private String code;

    ChattingSendReceive(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CallbackStatus of (String value) {
        for (CallbackStatus type : CallbackStatus.values()){
            if (type.getCode().equals(value))
                return type;
        }

        return null;
    }
}
