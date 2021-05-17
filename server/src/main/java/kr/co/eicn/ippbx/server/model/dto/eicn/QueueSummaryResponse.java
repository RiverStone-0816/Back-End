package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class QueueSummaryResponse {
	private String name; // QUEUE 이름
	private String hanName; // 큐그룹명
	private String number; // 큐번호
	private String subGroupName; // 예비큐 이름
	private String strategy; // 분배정책
	private Integer personCount = 0; // 사용자 수
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.BlendingMode
	 * */
	private String blendingMode; //
	private String host; // 해당 QUEUE 사용IP

	/**
	 * 자신이 속한 조직트리의 정보.
	 * 자신이 속한 조직트리의 레벨이 3이라면, List[0], List[1], List[2]의 정보가 전달되어야 한다.
	 */
	private List<OrganizationSummaryResponse> companyTrees;
}
