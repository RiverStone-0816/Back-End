package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * pds_group.last_upload_status
 * RINGALL:동시모두
 * LEASTRECENT:먼저끊은순서
 * FEWESTCALLS:적게받은순서
 * RRMEMORY:마지막통화한다음순서
 * RANDOM:랜덤
 * SEQUENCE:목록순서
 */
public enum PDSResultGroupStrategy implements CodeHasable<String> {
	RINGALL("ringall"),
	LEASTRECENT("leastrecent"),
	FEWESTCALLS("fewestcalls"),
	RRMEMORY("rrmemory"),
	RANDOM("random"),
	SEQUENCE("sequence");

	private final String code;

	PDSResultGroupStrategy(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
