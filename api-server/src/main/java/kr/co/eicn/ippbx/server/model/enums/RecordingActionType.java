package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

// 녹취 행동구분
public enum RecordingActionType implements CodeHasable<String> {
    LISTENING("L"), DOWNLOAD("D"), REMOVE("R");

    private String code;

    RecordingActionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
