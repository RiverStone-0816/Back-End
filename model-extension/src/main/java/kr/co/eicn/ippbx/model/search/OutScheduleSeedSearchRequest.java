package kr.co.eicn.ippbx.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutScheduleSeedSearchRequest extends PageQueryableForm {
    @PageQueryable
    private String name;      // 발신 스케쥴러 명
    /**
     * @see kr.co.eicn.ippbx.model.enums.DayOfWeek
     */
    @PageQueryable
    private String week;      // 요일(Mon:월요일, Tue:화요일, Wed:수용일, Thu:목요일, Fri:금요일, Sat:토요일, Sun:일요일)
    @JsonIgnore
    @PageQueryable
    private ScheduleType type; // 스케쥴 구분 W:주간, H:일별
}
