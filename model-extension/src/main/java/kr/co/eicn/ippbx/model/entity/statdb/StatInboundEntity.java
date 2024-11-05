package kr.co.eicn.ippbx.model.entity.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatInbound;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.GregorianCalendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatInboundEntity extends CommonStatInbound implements StatByTimeUnit {
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

    //평균 통화시간
    public Long getBillSecAvg() {
        return getSuccess() == 0 ? 0L : getBillsecSum() / getSuccess();
    }

    //평균 대기시간
    public Long getWaitAvg() {
        if (getSuccess() == 0 || getWaitSum() < getWaitCancelSum())
            return 0L;

        return (long) ((getWaitSum() - getWaitCancelSum()) / getSuccess());
    }

    //호응답률
    public Double getResponseRate() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getSuccess(), getConnreq())));
    }

    //콜백포함 응답률
    public Double getCallbackResponseRate() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getSuccess() + getCallback(), getConnreq())));
    }

    //서비스레벨 호응답률
    public Double getSvcLevelAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getServiceLevelOk(), getConnreq())));
    }

    //양적개선 응답률
    public Double getQuantitativeResponseRate() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getSuccess() + getCancelTimeout() + getCancelCustom(), getConnreq())));
    }

    //질적개선 응답률
    public Double getQualitativeResponseRate() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getSuccess() + getCancelNoanswer(), getConnreq())));
    }

    //단순조회율
    public Double getIvrAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getOnlyread(), getTotal())));
    }

    //호 포기율
    public Double getCancelAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getCancel(), getConnreq())));
    }

    public Integer getTotalSum() {
        return getTotal() + getCallback();
    }

    public float getAvgRate() {
        if (getSuccess() == null || getTotal() == null)
            return 0;

        return EicnUtils.getRateValue(getSuccess(), getTotal());
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
