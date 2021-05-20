package kr.co.eicn.ippbx.model.entity.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatMemberStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatMemberStatusEntity extends CommonStatMemberStatus implements StatByTimeUnit {
    private Calendar calendar = new GregorianCalendar();

    public Integer getYear() {
        calendar.setTime(getStatDate());
        return calendar.get(Calendar.YEAR);
    }

    public Integer getMonth() {
        calendar.setTime(getStatDate());
        return calendar.get(Calendar.MONTH) + 1;
    }

    public Integer getWeek() {
        Calendar firstDayOfWeek = new GregorianCalendar();
        firstDayOfWeek.setTime(getStatDate());
        firstDayOfWeek.set(Calendar.DAY_OF_MONTH, 1);
        int weekOfFirstDay = firstDayOfWeek.get(Calendar.DAY_OF_WEEK);

        calendar.setTime(getStatDate());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        return weekOfFirstDay > 4 ? weekOfMonth - 1 : weekOfMonth;
    }

    public Integer getDayOfWeek() {
        calendar.setTime(getStatDate());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
