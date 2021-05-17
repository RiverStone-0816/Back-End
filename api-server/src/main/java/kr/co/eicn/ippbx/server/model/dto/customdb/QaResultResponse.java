package kr.co.eicn.ippbx.server.model.dto.customdb;

import lombok.Data;

@Data
public class QaResultResponse {
    private String statDate;    //통계날짜
    private Integer count = 0;      //건수
}
