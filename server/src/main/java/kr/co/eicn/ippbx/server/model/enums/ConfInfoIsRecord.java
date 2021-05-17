package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 회의 상태
 */
public enum ConfInfoIsRecord implements CodeHasable<String> {
    녹취함("M"), 녹취안함("S");

    private String code;

    ConfInfoIsRecord(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
