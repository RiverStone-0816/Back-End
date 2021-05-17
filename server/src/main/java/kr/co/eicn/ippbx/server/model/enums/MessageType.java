package kr.co.eicn.ippbx.server.model.enums;

public enum MessageType {
    TEXT("text"), PHOTO("photo"), FILE("file"), AUDIO("audio"), VIDEO("video");

    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
