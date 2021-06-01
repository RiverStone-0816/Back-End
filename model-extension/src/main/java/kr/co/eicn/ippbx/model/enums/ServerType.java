package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * server_info.type
 * W:웹서버, P:PBX서버, V:웹보이스중계서버, K:상담톡릴레이서버, S:SMS서버
 * R:녹취서버, T:TTS서버, C:웹컨텐츠서버
 */
public enum ServerType implements CodeHasable<String> {
	WEB("W"), PBX("P"), WEB_VOICE_SERVER("V"), TALK_RELAY_SERVER("K"), SMS("S"),
	RECORD_SERVER("R"), TTS_SERVER("T"), WEB_CONTENT_SERVER("C");

	private final String code;

	ServerType(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
