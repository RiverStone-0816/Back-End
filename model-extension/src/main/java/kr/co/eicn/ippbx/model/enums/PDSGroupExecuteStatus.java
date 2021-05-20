package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * pds_group.last_execute_status
 * I:준비중, R:준비완료, P:진행중, S:정지, C:완료됨, D:마침, "":실행안됨
 */
public enum PDSGroupExecuteStatus implements CodeHasable<String> {
	PREPARING("I"), READY("R"), PROCEEDING("P"), STOP("S"), COMPLETE("C"), FINISHED("D"),
	UN_EXECUTED("");

	private String code;

	PDSGroupExecuteStatus(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
