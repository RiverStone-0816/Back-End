package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.TreeMap;

@Data
public class DashInboundChartResponse {
    private TreeMap<Byte, DashInboundChartDataResponse> inboundChat = new TreeMap<>();
}
