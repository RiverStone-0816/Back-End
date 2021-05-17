package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * NOANSWER: 비수신일 때 진행
 * ANSWER: 통화된 콜일 때 진행
 * ALL: 모든 콜 진행
 */
public enum InOutCallKind implements CodeHasable<String> {
    NOANSWER("NOANSWER"), ANSWER("ANSWER"), ALL("ALL");

    private String code;

    InOutCallKind(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
