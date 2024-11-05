package kr.co.eicn.ippbx.model.entity.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatUserOutbound;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatUserOutboundEntity extends CommonStatUserOutbound implements StatByTimeUnit {
    public Integer getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
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
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getStatDate());

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //비수신
    public Integer getFails() {
        if (getOutTotal() == null || getOutSuccess() == null)
            return 0;
        return getOutTotal() - getOutSuccess();
    }

    //평균 통화시간
    public long getAvgBillSec() {
        if (getOutBillsecSum() == null || getOutSuccess() == null || getOutSuccess() == 0)
            return 0;

        return (getOutSuccess() > 0 ? (long) (getOutBillsecSum() / getOutSuccess()) : 0);
    }

    //통화성공률
    public float getAvgRate() {
        if (getOutSuccess() == null || getOutTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getOutSuccess(), getOutTotal());
    }

    //콜백 성공률
    public float getCallbackProcessRate() {
        if (getCallbackCallSucc() == null || getCallbackCallCnt() == null)
            return 0;

        return EicnUtils.getRateValue(getCallbackCallSucc(), getCallbackCallCnt());
    }

    //예약 성공률
    public float getReserveProcessRate() {
        if (getReserveCallSucc() == null || getReserveCallCnt() == null)
            return 0;

        return EicnUtils.getRateValue(getReserveCallSucc(), getReserveCallCnt());
    }
}
