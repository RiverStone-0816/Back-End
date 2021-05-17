package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 *  NORMAL: 정상통화, NON_RECEIVING: 비수신
 */
public enum NormalCallStatus implements CodeHasable<String> {
    NORMAL("Y"), NON_RECEIVING("N");

    private String code;

    NormalCallStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
