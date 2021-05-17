package kr.co.eicn.ippbx.server.model.enums;

import kr.co.eicn.ippbx.server.util.CodeHasable;

/**
 * 조직권한부여여부
 * A:모든조직허용 - 조직에 관계없이 모든 데이터를 조회할수 있음
 * G:권한을 부여받은 조직만 허용 - 설정된 단계의 설정된조직 하위 조직에 대한 데이터 권한을 갖음
 * M:자신것만 허용 - 자신에 관계된 녹취를 듣는다거나 데이터 권한을 갖음
 */
public enum GroupLevelAuth implements CodeHasable<String> {
	ALL("A"), ALLOW_ONLY_AUTHORIZED_ORGANIZATIONS("G"), ALLOW_YOURSELF_ONLY("M");

	private String code;

	GroupLevelAuth(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
