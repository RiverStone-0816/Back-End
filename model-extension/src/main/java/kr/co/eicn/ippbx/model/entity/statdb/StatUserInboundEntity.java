package kr.co.eicn.ippbx.model.entity.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatUserInbound;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatUserInboundEntity extends CommonStatUserInbound implements StatByTimeUnit {
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

    //평균 통화시간
    public long getAvgBillSec() {
        if (getInBillsecSum() == null || getInSuccess() == null || getInSuccess() == 0)
            return 0;

        return getInBillsecSum() / getInSuccess();
    }

    //평균 대기시간
    public Integer getAvgWaitSec() {
        if (getInWaitsecSum() == null || getInTotal() == null || getInTotal() == 0)
            return 0;

        return getInWaitsecSum() / getInTotal();
    }

    //개인비수신
    public Integer getCancel() {
        return getInHuntNoanswer();
    }

    //응대율
    public float getAvgRate() {
        if (getInSuccess() == null || getInTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getInSuccess(), getInTotal());
    }

    //전환율
    public float getTransferCountRate() {
        if (getTransferer() == null || getInTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getTransferer(), getInTotal());
    }
}
