package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroupList;
import kr.co.eicn.ippbx.server.model.MonitorIvrTree;
import lombok.Data;

@Data
public class MonitorIvrScheduleGroupListResponse {
    private ScheduleGroupList scheduleGroupList;

    private MonitorIvrTree monitorIvrTree;
    private QueueDetailResponse queue; // 유형설정이 번호직접연결(내부번호연결)
}
