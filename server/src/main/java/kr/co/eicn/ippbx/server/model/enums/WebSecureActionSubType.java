package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

/**
 * OK: 성공
 * ADD: 추가
 * MOD: 수정
 * DEL: 삭제
 * DOWN: 다운로드
 * DIST: 분배
 * REDIST: 재분배
 * PLAY: 음원듣기
 * SAVE_ENC_KEY: 암호키 적용
 * ENABLE_ENC: 암호 활성화
 * NO_USERID: 아이디 불일치
 * NO_EXTEN: 내선 불일치
 * NO_PHONE_NUMBER: ARS인증을 위한 휴대폰번호 미등록
 * WRONG_COMPANYID: 고객사 불일치
 * WRONG_PASSWORD: 패스워드 불일치
 * LOGIN_DENIED: 로그인 일시 정지
 */
public enum WebSecureActionSubType implements CodeHasable<String> {
    OK("OK"),
    ADD("ADD"),
    MOD("MOD"),
    DEL("DEL"),
    DOWN("DOWN"),
    DIST("DIST"),
    REDIST("REDIST"),
    PLAY("PLAY"),
    SAVE_ENC_KEY("SAVE_ENC_KEY"),
    ENABLE_ENC("ENABLE_ENC"),
    NO_USERID("NO_USERID"),
    NO_EXTEN("NO_EXTEN"),
    NO_PHONE_NUMBER("NO_PHONE_NUMBER"),
    WRONG_COMPANYID("WRONG_COMPANYID"),
    WRONG_PASSWORD("WRONG_PASSWORD"),
    LOGIN_DENIED("LOGIN_DENIED");

    private final String code;

    WebSecureActionSubType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static WebSecureActionSubType of (String code) {
        for (WebSecureActionSubType type : WebSecureActionSubType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
