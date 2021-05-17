package kr.co.eicn.ippbx.server.model.entity.statdb;

import java.sql.Date;

public interface StatByTimeUnit {
    public Integer getYear();
    public Integer getMonth();
    public Integer getWeek();
    public Integer getDayOfWeek();
    public Date getStatDate();
    public Byte getStatHour();
}
