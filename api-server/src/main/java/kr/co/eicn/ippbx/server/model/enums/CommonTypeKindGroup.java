package kr.co.eicn.ippbx.server.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 유형 그룹
 */
public enum CommonTypeKindGroup {
	APPLICATION("상담어플리케이션", Arrays.asList(CommonTypeKind.MAIN_DB, CommonTypeKind.LINK_DB, CommonTypeKind.CONSULTATION_RESULTS)),
	OUTBOUND("아웃바운드", Arrays.asList(CommonTypeKind.PDS, CommonTypeKind.PREVIEW, CommonTypeKind.CONSULTATION_RESULTS));

	private String title;
	private List<CommonTypeKind> commonTypeKinds;

	CommonTypeKindGroup(String title, List<CommonTypeKind> commonTypeKinds) {
		this.title = title;
		this.commonTypeKinds = commonTypeKinds;
	}

	public static CommonTypeKindGroup findByCommonTypeKind(CommonTypeKindGroup group, CommonTypeKind commonTypeKind) {
		return Arrays.stream(CommonTypeKindGroup.values())
				.filter(e -> Objects.equals(e, group))
				.filter(e -> e.hasCommonTypeKind(commonTypeKind))
				.findAny()
				.orElse(null);
	}

	public boolean hasCommonTypeKind(CommonTypeKind commonTypeKind) {
		return commonTypeKinds.stream()
				.anyMatch(e -> Objects.equals(e, commonTypeKind));
	}

	public String getTitle() {
		return title;
	}
}
