package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 회의 상태
 */
public enum ConfInfoIsMachineDetect implements CodeHasable<String> {
    머신디텍트함("Y"), 버튼눌러참여("N");
    private String code;

    ConfInfoIsMachineDetect(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
