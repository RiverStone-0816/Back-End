package kr.co.eicn.ippbx.server.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * IVR_TREE TYPE GROUP
 */
public enum IvrTreeTypeGroup {
	IVR_TREE("IVR_TREE", Arrays.asList(IvrTreeType.values())),
	PDS_IVR("PDS_IVR", Arrays.asList(IvrTreeType.RE_LISTENING_TO_THE_MENU, IvrTreeType.CALL_CONNECTION_BY_CONNECTION_NUMBER
			, IvrTreeType.VOICE_GUIDANCE_EXIT, IvrTreeType.LINK_TO_ANOTHER_IVR, IvrTreeType.RETURN_TO_THE_PREVIOUS_STEP
			, IvrTreeType.RETURN_TO_THE_FIRST_STEP, IvrTreeType.RECORD_PLAY));

	private final String title;
	private final List<IvrTreeType> ivrTreeTypes;

	IvrTreeTypeGroup(String title, List<IvrTreeType> ivrTreeTypes) {
		this.title = title;
		this.ivrTreeTypes = ivrTreeTypes;
	}

	public static IvrTreeTypeGroup findByIvrTreeTypeGroup(IvrTreeTypeGroup group, IvrTreeType type) {
		return Arrays.stream(IvrTreeTypeGroup.values())
				.filter(e -> Objects.equals(e, group))
				.filter(e -> e.hasIvrTreeType(type))
				.findAny()
				.orElse(null);
	}

	public boolean hasIvrTreeType(IvrTreeType type) {
		return ivrTreeTypes.stream()
				.anyMatch(e -> Objects.equals(e, type));
	}

	public String getTitle() {
		return title;
	}

	public List<IvrTreeType> getIvrTreeTypes() {
		return ivrTreeTypes;
	}
}
