package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

import java.util.Objects;

/**
 * LOGIN: 로그인
 * GROUP: 조직 관리
 * NUMBER: 번호 관리
 * PHONE: 내선 관리
 * CALLBACK: 콜백 관리
 * IVR: IVR 관리
 * SEARCH_CUSTOM: 녹취 관리
 * CUSTOM: 고객DB 관리
 * SOUND: 음원 관리
 * ADMIN: 어드민 관리
 * PERSON: 상담원 관리
 * EMAIL: 이메일 관리
 * TALK: 상담톡 관리
 * PICKUP: 당겨 받기
 * QUEUE: 큐 설정
 * WEBLOG: 웹 로그 관리
 * LOGIN_LOG: 로그인이력
 * RECORD_ENC: 녹취 암호화
 * RECORD_MULTI_DOWN: 녹취 일괄 다운
 * RECORD_FILE: 녹취 파일
 * PDS_GROUP: PDS 그룹
 * PDS_IVR: PDS IVR
 * CID_INFO: 내선 기타 정보
 */
public enum WebSecureActionType implements CodeHasable<String> {
    LOGIN("LOGIN"),
    GROUP("GROUP"),
    NUMBER("NUMBER"),
    PHONE("PHONE"),
    CALLBACK("CALLBACK"),
    IVR("IVR"),
    SEARCH("SEARCH_CUSTOM"),
    CUSTOM("CUSTOM"),
    SOUND("SOUND"),
    ADMIN("ADMIN"),
    PERSON("PERSON"),
    EMAIL("EMAIL"),
    TALK("TALK"),
    PICKUP("PICKUP"),
    QUEUE("QUEUE"),
    WEBLOG("WEBLOG"),
    LOGIN_LOG("LOGIN_LOG"),
    RECORD_ENC("RECORD_ENC"),
    RECORD_DOWN("RECORD_MULTI_DOWN"),
    RECORD_FILE("FILE"),
    PDS_GROUP("PDS_GROUP"),
    PDS_IVR("PDS_IVR"),
    CID("CID_INFO");

    private final String code;

    WebSecureActionType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static WebSecureActionType of (String code) {
        for (WebSecureActionType type : WebSecureActionType.values()) {
            if (Objects.equals(type.getCode(), code))
                return type;
        }

        return null;
    }
}
