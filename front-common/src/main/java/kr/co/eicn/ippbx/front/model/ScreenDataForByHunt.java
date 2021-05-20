package kr.co.eicn.ippbx.front.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ScreenDataForByHunt {
    private String representativeServiceName;
    private String representativeServiceNumber;
    private List<HuntData> hunts = new ArrayList<>();
    private List<ServiceData> services = new ArrayList<>();

    @Data
    public static class HuntData {
        private String queueName;
        private String queueKoreanName;
        private String queueServiceNumber;
        private Map<Integer, Integer> constantStatusCounts = new HashMap<>();
        private Integer customerWaiting = 0; // 대기고객수
    }

    @Data
    public static class ServiceData {
        private String serviceName;
        private Integer customerWaiting;
        private Integer totalCall;
        private Integer onlyReadCall;
        private Integer connectionRequest;
        private Integer successCall;
        private Integer cancelCall;
        private Double responseRate;
    }
}
