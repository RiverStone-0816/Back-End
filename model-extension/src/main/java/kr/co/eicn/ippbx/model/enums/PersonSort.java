package kr.co.eicn.ippbx.model.enums;

import kr.co.eicn.ippbx.util.CodeHasable;

/**
 * 사용자 정렬 방식
 * 아이디, 성명, 내선, 소속, 로그인
 */
public enum PersonSort implements CodeHasable<String> {
    ID("id"), NAME("id_name"), PEER("peer"), GROUP("group_code"), LOGIN("is_login");
	private final String code;

	PersonSort(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}