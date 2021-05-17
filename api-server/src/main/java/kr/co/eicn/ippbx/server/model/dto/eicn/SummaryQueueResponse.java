package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryQueueResponse {
    private String name;
    private String hanName;
    private String number;
    private String svcNumber;
}