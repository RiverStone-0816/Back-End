package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * research_item.sound_kind 음원구분
 * N:사용안함, T:TTS로읽음, S:등록된음원사용
 */
public enum ResearchItemSoundKind implements CodeHasable<String> {
	NONE("N"), TTS("T"), SOUND("S");

	private String code;

	ResearchItemSoundKind(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
