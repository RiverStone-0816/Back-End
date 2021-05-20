package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class StatHourGraphData {
    private String queueName;
    private Map<Byte, Integer> dataMap = new LinkedHashMap<>();
}
