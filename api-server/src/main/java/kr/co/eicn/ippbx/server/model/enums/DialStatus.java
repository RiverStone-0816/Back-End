package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

public enum DialStatus implements CodeHasable<Byte> {
    WAIT((byte) 0), POST_PROCESS((byte) 2);

    private Byte code;

    DialStatus(byte code) {
        this.code = code;
    }

    @Override
    public Byte getCode() {
        return code;
    }

    public static DialStatus of (byte value) {
        for (DialStatus type : DialStatus.values()) {
            if (type.code == value)
                return type;
        }

        return null;
    }
}
