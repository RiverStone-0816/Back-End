package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NumberOfWaitingHourResponse {
    private Map<Integer, Integer> waitCountMap;
}