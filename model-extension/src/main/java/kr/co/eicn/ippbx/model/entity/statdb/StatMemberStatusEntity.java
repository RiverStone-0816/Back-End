package kr.co.eicn.ippbx.model.entity.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatMemberStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatMemberStatusEntity extends CommonStatMemberStatus implements StatByTimeUnit {
    public Integer getYear() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getStatDate());

        return calendar.get(Calendar.YEAR);
    }

    public Integer getMonth() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getStatDate());

        return calendar.get(Calendar.MONTH) + 1;
    }

    public Integer getMonthByWeek() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getStatDate());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);

        Calendar firstDayOfWeek = new GregorianCalendar();
        firstDayOfWeek.setTime(calendar.getTime());
        firstDayOfWeek.set(Calendar.DAY_OF_MONTH, 1);
        int weekOfFirstDay = firstDayOfWeek.get(Calendar.DAY_OF_WEEK);

        return calendar.get(Calendar.WEEK_OF_MONTH) == 1 && weekOfFirstDay > 4 ? calendar.get(Calendar.MONTH) : calendar.get(Calendar.MONTH) + 1;
    }

    public Integer getWeek() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getStatDate());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        Calendar firstDayOfMonth = new GregorianCalendar();
        firstDayOfMonth.setTime(getStatDate());
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        int weekOfFirstDay = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);

        return calendar.get(Calendar.MONTH) == firstDayOfMonth.get(Calendar.MONTH) && weekOfFirstDay > 4 ? weekOfMonth - 1 : weekOfMonth;
    }

    public Integer getDayOfWeek() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(getStatDate());

        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
