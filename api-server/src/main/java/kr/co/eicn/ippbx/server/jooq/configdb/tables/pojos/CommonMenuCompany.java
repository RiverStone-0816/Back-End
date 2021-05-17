package kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CommonMenuCompany implements Serializable {
	private Integer seq;                // SEQUENCE KEY
	private String  userid;             // 사용자아이디
	private String  menuCode;           // 메뉴코드
	private String  menuName;           // 메뉴명
	private String  menuTreeName;       // 윗레벨의 코드를 포함한 코드의 나열 ex>0003_0008_0001
	private Integer menuLevel;          // 메뉴레벨
	private String  parentMenuCode;     // 상위메뉴코드
	private String  parentTreeName;     // 윗레벨 코드의 나열 ex>0003_0008
	private String  menuActionExeId;    // 연결액션
	private Integer sequence;           // 정렬순서
	private String  viewYn;             // 보임여부
	private String  icon;               //
	private String  actionType;         // PAGE, MENU, POP
	private String  authType;           // 권한타입
	private String  groupLevelAuthYn;   // 조직권한부여여부
	private String  groupCode;          // 조직코드
	private Integer groupLevel;         // 조직레벨

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CommonMenuCompany that = (CommonMenuCompany) o;
		return Objects.equals(menuCode, that.menuCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(menuCode);
	}
}
