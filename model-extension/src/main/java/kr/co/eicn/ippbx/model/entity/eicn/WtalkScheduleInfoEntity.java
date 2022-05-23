package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkScheduleInfo;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkScheduleInfoEntity extends WtalkScheduleInfo {
	private WtalkScheduleGroupEntity scheduleGroup; // 상담톡유형정보
	private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
}
