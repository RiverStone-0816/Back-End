package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleInfo;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleInfoEntity extends ScheduleInfo {
	private ScheduleGroupEntity scheduleGroup; // 스케쥴 유형정보
	private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
}
