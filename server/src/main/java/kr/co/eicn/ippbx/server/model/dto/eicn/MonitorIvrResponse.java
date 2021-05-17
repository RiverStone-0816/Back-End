package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleInfo;
import lombok.Data;

import java.util.List;

/**
 * TODO:
 */
@Data
public class MonitorIvrResponse {
    private ScheduleInfo scheduleInfo; // 스케쥴 유형정보
    private ScheduleGroup scheduleGroup;
    private List<MonitorIvrScheduleGroupListResponse> scheduleContents;
}
