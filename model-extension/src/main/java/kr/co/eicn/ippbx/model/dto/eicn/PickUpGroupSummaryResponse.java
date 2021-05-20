package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class PickUpGroupSummaryResponse {
	private Integer seq;
	private Integer groupcode;  // 당겨받기 그룹코드
	private String groupname;  // 당겨받기 그룹명
	private String  groupCode;
	private String  groupTreeName;
	private Integer groupLevel;
	private String host;       // 교환기 host
	private String hostName;   // 교환기명

	/**
	 * 자신이 속한 조직트리의 정보.
	 * 자신이 속한 조직트리의 레벨이 3이라면, List[0], List[1], List[2]의 정보가 전달되어야 한다.
	 */
	private List<OrganizationSummaryResponse> companyTrees;
	private Integer personCount; // 구성인원수

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupcode(Integer groupcode) {
		this.groupcode = groupcode;
	}

	public Integer getGroupcode() {
		return groupcode;
	}
}
