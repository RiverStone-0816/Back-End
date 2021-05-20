package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum ChattingMsgType implements CodeHasable<String> {
    TEXT("text"), PHOTO("photo"), AUDIO("audio"), FILE("file");

    private String code;

    ChattingMsgType(String code) {
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
