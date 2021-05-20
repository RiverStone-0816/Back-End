package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum LogoutStatus implements CodeHasable<Byte> {
    WAIT((byte) 0), LOGOUT((byte) 9);

    private Byte code;

    LogoutStatus(Byte code) {
        this.code = code;
    }

    @Override
    public Byte getCode() {
        return code;
    }

    public static LogoutStatus of (byte value) {
        for (LogoutStatus type : LogoutStatus.values()) {
            if (type.code == value)
                return type;
        }

        return null;
    }
}
