package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum CallbackStatus implements CodeHasable<String> {
    NONE("A"), COMPLETE("B"), PROCESSING("C");

    private String code;

    CallbackStatus(String code) {
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
