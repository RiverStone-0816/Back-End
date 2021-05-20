package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * PREPARE_ACTIVE: 채팅방 생성 후 메시지 주고 받기 전 상태
 * ACTIVE: 메시지 주고 받는 상태 (활성화)
 * INACTIVE: 로그아웃한 상태 (비활성화)
 * LEAVE: 채팅방 나간 상태
 */
public enum ChattingJoinStatus implements CodeHasable<String> {
    PREPARE_ACTIVE("S"), ACTIVE("Y"), INACTIVE("N"), LEAVE("X");

    private String code;

    ChattingJoinStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CallbackStatus of (String value) {
        for (CallbackStatus type : CallbackStatus.values()){
            if (type.getCode().equals(value))
                return type;
        }

        return null;
    }
}
