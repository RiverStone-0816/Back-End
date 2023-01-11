package kr.co.eicn.ippbx.model.entity.statdb;

import java.sql.Date;

public interface StatByTimeUnit {
    Integer getYear();
    Integer getMonth();
    Integer getMonthByWeek();
    Integer getWeek();
    Integer getDayOfWeek();
    Date getStatDate();
    Byte getStatHour();
}
