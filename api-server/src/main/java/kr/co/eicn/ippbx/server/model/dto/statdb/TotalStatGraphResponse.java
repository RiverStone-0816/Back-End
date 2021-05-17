package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class TotalStatGraphResponse {
    private StatHourGraphData huntAHourData;
    private StatHourGraphData huntBHourData;
    private StatWeekGraphData huntAWeekData;
    private StatWeekGraphData huntBWeekData;
}