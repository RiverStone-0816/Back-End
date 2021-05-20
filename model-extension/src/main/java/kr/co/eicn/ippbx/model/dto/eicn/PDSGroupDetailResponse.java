package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDSGroupDetailResponse extends PdsGroup {
	private String pdsTypeName; // 업로드유형명
	private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
}
