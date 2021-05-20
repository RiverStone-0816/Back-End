package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitorQueueSummaryPerson {
    private String  queueName;  //헌트명
    private List<PersonListSummary> personList;
    private Integer billSec = 0;    //상담원 통화시간
}
