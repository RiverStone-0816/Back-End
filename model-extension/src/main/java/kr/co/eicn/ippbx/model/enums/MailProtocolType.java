package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

import java.util.Objects;

/**
 * 수신 메일 프로토콜
 */
public enum MailProtocolType implements CodeHasable<String> {
    SMPT("SMPT"), POP3("POP3"), IMAP("IMAP");

    private final String code;

    MailProtocolType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static MailProtocolType of(String value) {
        for (MailProtocolType type : MailProtocolType.values()) {
            if (Objects.equals(type.code, value))
                return type;
        }

        return null;
    }
}
