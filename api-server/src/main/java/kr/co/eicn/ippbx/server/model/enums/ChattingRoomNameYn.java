package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum ChattingRoomNameYn implements CodeHasable<String> {
    CHANGE("Y"), CHANGE_N("N");

    private String code;

    ChattingRoomNameYn(String code) {
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
