package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkScheduleInfo;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleInfoEntity extends TalkScheduleInfo {
	private TalkScheduleGroupEntity scheduleGroup; // 상담톡유형정보
	private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
}
