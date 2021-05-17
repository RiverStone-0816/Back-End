package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
public class DashCurrentResultCallResponse {
    private String title;
    private Integer inCallingCount = 0;  //I/B 통화중
    private Integer outCallingCount = 0;  //O/B 통화중

    private TreeMap<Integer, DashResultCallChartResponse> hourToResultCall = new TreeMap<>();
}
