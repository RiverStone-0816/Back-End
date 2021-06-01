package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 *  메일 프로토콜
 */
public enum MailProtocolType implements CodeHasable<String> {
    SMTP("S"), POP3("P"), IMAP("I");

    private final String code;

    MailProtocolType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
