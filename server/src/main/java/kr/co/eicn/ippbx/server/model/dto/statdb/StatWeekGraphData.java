package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class StatWeekGraphData {
    private String queueName;
    private Map<Byte, Integer> dataMap = new LinkedHashMap<>();
}
