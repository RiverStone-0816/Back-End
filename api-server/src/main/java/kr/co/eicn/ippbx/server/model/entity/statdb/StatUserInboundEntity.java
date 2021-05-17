package kr.co.eicn.ippbx.server.model.entity.statdb;

import kr.co.eicn.ippbx.server.jooq.statdb.tables.pojos.CommonStatUserInbound;
import kr.co.eicn.ippbx.server.util.EicnUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatUserInboundEntity extends CommonStatUserInbound implements StatByTimeUnit {
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

    public long getAvgBillSec() {
        if (getInBillsecSum() == null || getInSuccess() == null || getInSuccess() == 0)
            return 0;

        return getInBillsecSum() / getInSuccess();
    }

    public Integer getCancel() {
        return getInTotal() - getInSuccess();
    }

    public float getAvgRate() {
        if (getInSuccess() == null || getInTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getInSuccess(), getInTotal());
    }

    public Integer getAvgWaitSec() {
        if (getInWaitsecSum() == null || getInTotal() == null || getInTotal() == 0)
            return 0;

        return getInWaitsecSum() / getInTotal();
    }

    public float getTransferCountRate() {
        if (getTransferer() == null || getInTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getTransferer(), getInTotal());
    }
}
