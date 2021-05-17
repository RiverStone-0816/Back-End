package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Data
public class DashTotalStatResponse extends DashServiceStatResponse{
    private TreeMap<Integer, Float> hourToResponseRate = new TreeMap<>(Comparator.comparingInt(e -> e));
}
