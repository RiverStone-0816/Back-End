package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.entity.eicn.Organization;
import lombok.Data;

import java.util.List;
/**
 * 계층 구조상의 전체 부모 구성원들 정보와 자식 구성원 및 사용자들의 숫자를 카운팅한다.
 */
@Data
public class OrganizationPersonSummaryResponse {
	/**
	 * 전체 부모 구성원 정보.
	 * 자신이 레벨3 구성원일 때, List[0]은 레벨1 구성원 정보, List[1]은 레벨2 구성원 정보이다.
	 */
	private List<Organization> parents;
	private List<MemberSummaryResponse> memberSummaries;

	/**
	 * 자식 구성원들의 계층별 수.
	 * 자신이 레벨3 구성원일 때, List[0]은 레벨4 구성원의 수, List[1]은 레벨5 구성원의 수 등이 된다.
	 */
	private List<Integer> countsOfChildren;
	/**
	 * 자신에게 속한 사용자 숫자
	 */
	private Integer userCountBelonging;
}
