package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleInfo;
import lombok.Data;

import java.util.List;

@Data
public class MonitorIvrResponse {
    private ScheduleInfo scheduleInfo; // 스케쥴 유형정보
    private ScheduleGroup scheduleGroup;
    private List<MonitorIvrScheduleGroupListResponse> scheduleContents;
}
