package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 *  PROCESS : 진행중인대화방 > 상태값
 *  STOP : 종료된대화방 > 상태값
 *  PROCESS_OR_STOP : 진행/종료된대화방 > 검색조건
 *  DOWN : 내려진대화방 > 상태값/검색조건
 * */
public enum RoomStatus implements CodeHasable<String> {
    PROCESS("S"), STOP("E"), PROCESS_OR_STOP("C"), DOWN("X"), BOT_PLAY("B"), SERVICE_BY_GROUP_CONNECT("G");

    private final String code;

    RoomStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
