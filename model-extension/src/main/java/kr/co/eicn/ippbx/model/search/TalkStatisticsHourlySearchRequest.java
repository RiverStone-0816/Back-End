package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkStatisticsHourlySearchRequest extends TalkStatisticsSearchRequest {
    @PageQueryable
    private Byte startHour;
    @PageQueryable
    private Byte endHour = 23;
}
