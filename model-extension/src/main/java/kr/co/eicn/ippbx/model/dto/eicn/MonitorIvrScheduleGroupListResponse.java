package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList;
import kr.co.eicn.ippbx.model.MonitorIvrTree;
import lombok.Data;

@Data
public class MonitorIvrScheduleGroupListResponse {
    private ScheduleGroupList scheduleGroupList;

    private MonitorIvrTree monitorIvrTree;
    private QueueDetailResponse queue; // 유형설정이 번호직접연결(내부번호연결)
}
