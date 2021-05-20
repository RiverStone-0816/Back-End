package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 *  OK : 데몬 정상
 *  NOK : 데몬 비정상
 * */
public enum DaemonStatus implements CodeHasable<String> {
    OK("OK"), NOK("NOK");

    private String code;

    DaemonStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
