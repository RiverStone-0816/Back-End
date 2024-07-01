package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryQueueResponse {
    private String name;
    private String hanName;
    private String number;
    private String svcNumber;
    private String groupCode;
    private String groupTreeName;
}
