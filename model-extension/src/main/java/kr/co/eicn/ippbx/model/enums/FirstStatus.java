package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

public enum FirstStatus  implements CodeHasable<Byte> {
    WAIT((byte) 0), POST_PROCESS((byte) 2);

    private Byte code;

    @Override
    public Byte getCode() {
        return code;
    }

    FirstStatus(Byte code) {
        this.code = code;
    }

    public static FirstStatus of (byte value) {
        for (FirstStatus type : FirstStatus.values()) {
            if (type.code == value)
                return type;
        }

        return null;
    }
}
