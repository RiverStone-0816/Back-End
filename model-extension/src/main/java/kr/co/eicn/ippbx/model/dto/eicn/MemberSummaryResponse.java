package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberSummaryResponse extends CompanyTree {
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
