package kr.co.eicn.ippbx.server.model.enums;

/**
 * UPLOAD:
 * PICKUP_UPDATE: 아스터리스크 상태 업데이트?
 * RELOAD_RING_BACK_TONE:
 * RECORD_MULTI_DOWNLOAD:
 * SCP_GET_SINGLE_FILE:
 * SCP_SOUND_MOH: pbx와 음원파일 동기화
 */
public enum ShellCommand {
	UPLOAD("/home/ippbxmng/lib/common_upload.sh"), PICKUP_UPDATE("/home/ippbxmng/lib/pickup_update.sh"),
	RELOAD_RING_BACK_TONE("/home/ippbxmng/lib/reload_moh.sh"), RECORD_MULTI_DOWNLOAD("/home/ippbxmng/lib/record_multi_download.sh"),
	SCP_GET_SINGLE_FILE("/home/ippbxmng/lib/scp_get_single.sh"),
	SCP_SOUND_MOH("/home/ippbxmng/lib/scp_sound_moh.sh");

	private String url;

	ShellCommand(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
