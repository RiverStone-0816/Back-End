package kr.co.eicn.ippbx.server.model.entity.statdb;

import kr.co.eicn.ippbx.server.jooq.statdb.tables.pojos.CommonStatInbound;
import kr.co.eicn.ippbx.server.util.EicnUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatInboundEntity extends CommonStatInbound implements StatByTimeUnit {
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

    //평균
    public Long getBillSecAvg() {
        return getSuccess() == 0 ? 0L : getBillsecSum() / getSuccess();
    }

    public Long getWaitAvg() {
        return getSuccess() == 0 ? 0L : getWaitSum() / getSuccess();
    }

    public Double getResponseRate() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getSuccess(), getConnreq())));
    }

    public Double getIvrAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getOnlyread(), getTotal())));
    }

    public Double getSvcLevelAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getServiceLevelOk(), getConnreq())));
    }

    public Integer getTotalSum() {
        return getTotal() + getCallback();
    }

    @Override
    public String toString() {
        return "StatInboundEntity{" +
                "getYear=" + getYear() + "," +
                "getMonth=" + getMonth() + "," +
                "getWeek=" + getWeek() + "," +
                "getDayOfWeek=" + getDayOfWeek() + "," +
                "getBillSecAvg=" + getBillSecAvg() + "," +
                "getWaitAvg=" + getWaitAvg() + "," +
                "getResponseRate=" + getResponseRate() + "," +
                "getIvrAvg=" + getIvrAvg() + "," +
                "getSvcLevelAvg=" + getSvcLevelAvg() + "," +
                "getCancel=" + getCancel() + "," +
                "getSuccess=" + getSuccess() + "," +
                "getConnreq=" + getConnreq() + "," +
                "getSuccess=" + getSuccess() + "," +
                "getTotalSum=" + getTotalSum() +
                '}';
    }
}