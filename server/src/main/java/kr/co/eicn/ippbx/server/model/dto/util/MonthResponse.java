package kr.co.eicn.ippbx.server.model.dto.util;

import lombok.Data;

import java.sql.Date;

@Data
public class MonthResponse {
    private Date startDateOfMonth;  //해당 건수의 리스트 팝업 표시에 필요한 데이터
    private Date endDateOfMonth;    //해당 건수의 리스트 팝업 표시에 필요한 데이터
    private Integer year;
    private Integer month;

    @Override
    public String toString() {
        return year + "년 " + month + "월";
    }
}