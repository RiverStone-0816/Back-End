package kr.co.eicn.ippbx.server.model.entity.statdb;

import kr.co.eicn.ippbx.server.jooq.statdb.tables.pojos.CommonStatUserOutbound;
import kr.co.eicn.ippbx.server.util.EicnUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatUserOutboundEntity extends CommonStatUserOutbound implements StatByTimeUnit {
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

    public Integer getFails() {
        if (getOutTotal() == null || getOutSuccess() == null)
            return 0;
        return getOutTotal() - getOutSuccess();
    }

    public long getAvgBillSec() {
        if (getOutBillsecSum() == null || getOutSuccess() == null || getOutSuccess() == 0)
            return 0;

        return (getOutSuccess() > 0 ? (long) (getOutBillsecSum() / getOutSuccess()) : 0);
    }

    public float getAvgRate() {
        if (getOutSuccess() == null || getOutTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getOutSuccess(), getOutTotal());
    }

    public float getCallbackProcessRate() {
        if (getCallbackCallSucc() == null || getCallbackCallCnt() == null)
            return 0;

        return EicnUtils.getRateValue(getCallbackCallSucc(), getCallbackCallCnt());
    }

    public float getReserveProcessRate() {
        if (getReserveCallSucc() == null || getReserveCallCnt() == null)
            return 0;

        return EicnUtils.getRateValue(getReserveCallSucc(), getReserveCallCnt());
    }
}
