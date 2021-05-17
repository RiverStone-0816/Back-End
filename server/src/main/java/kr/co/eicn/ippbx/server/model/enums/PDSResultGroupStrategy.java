package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * pds_group.last_upload_status
 * "":지정안됨, rrmemory:마지막통화한다음순서(roundrobin), ringall:동시모두, sequence:목록순서, leastrecent:최소한최근에받은순서, fewestcalls:연결횟수가적은순서, random:랜덤
 */
public enum PDSResultGroupStrategy implements CodeHasable<String> {
	DO_NOT_STRATEGY(""), RRMEMORY("rrmemory"), RINGALL("ringall"), SEQUENCE("sequence"),
	LEASTRECENT("leastrecent"), FEWESTCALLS("fewestcalls"), RANDOM("fewestcalls");

	private String code;

	PDSResultGroupStrategy(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
