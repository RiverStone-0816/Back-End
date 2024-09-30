package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryCallbotResponse {
    private String serviceCode;
    private String serviceName;
    private String callbotKey;
}
