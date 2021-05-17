package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 *  메일 프로토콜
 */
public enum MailProtocolType implements CodeHasable<String> {
    SMTP("S"), POP3("P"), IMAP("I");

    private String code;

    MailProtocolType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
