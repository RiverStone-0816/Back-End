package kr.co.eicn.ippbx.front.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ScreenDataForIntegration {
    private String serviceName;
    private String serviceNumber;
    private Map<Integer, Integer> constantStatusCounts = new HashMap<>();

    private Integer customerWaiting;
    private Integer inboundCall;
    private Integer connectionRequest;
    private Integer successCall;
    private Integer cancelCall;
    private Double responseRate;
}
