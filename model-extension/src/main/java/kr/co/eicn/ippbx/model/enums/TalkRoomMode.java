package kr.co.eicn.ippbx.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.eicn.ippbx.util.CodeHasable;

//상담톡 리스트 모드(MY:상담중, END:종료, TOT:비접수, OTH:타상담)
public enum TalkRoomMode implements CodeHasable<String> {
    MINE("MY"), END("END"), TOT("TOT"), OTHER("OTH");

    private String code;

    TalkRoomMode(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
