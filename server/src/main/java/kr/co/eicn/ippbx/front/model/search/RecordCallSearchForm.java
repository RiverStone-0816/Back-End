package kr.co.eicn.ippbx.front.model.search;

import kr.co.eicn.ippbx.server.model.search.RecordCallSearch;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Calendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordCallSearchForm extends RecordCallSearch {
    @PageQueryable
    private Date startDate;   //시작날짜
    @PageQueryable
    private Integer startHour = 0;
    @PageQueryable
    private Date endDate;     //종료날짜
    @PageQueryable
    private Integer endHour = 23;

    @PageQueryable
    private Integer callStartMinutes;
    @PageQueryable
    private Integer callStartSeconds;
    @PageQueryable
    private Integer callEndMinutes;
    @PageQueryable
    private Integer callEndSeconds;

    public RecordCallSearchForm() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        startDate = new Date(calendar.getTimeInMillis());
        endDate = new Date(calendar.getTimeInMillis());
    }
}
