package kr.co.eicn.ippbx.server.model.dto.util;

import lombok.Data;

import java.sql.Date;

@Data
public class WeekResponse {
    private Date startDateOfWeek;   //해당 건수의 리스트 팝업 표시에 필요한 데이터
    private Date endDateOfWeek;     //해당 건수의 리스트 팝업 표시에 필요한 데이터
    private Integer year;
    private Integer month;
    private Integer weekOfMonth;

    public void setWeekInfo(Integer year, Integer month, Integer week) {
        this.year = year;
        this.month = month;
        this.weekOfMonth = week;
    }

    @Override
    public String toString() {
        return year + "년 " + month + "월 " + weekOfMonth + "주차";
    }
}
