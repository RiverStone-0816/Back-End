package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.TreeMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class DashTotalStatResponse extends DashServiceStatResponse {
    private TreeMap<Integer, Float> hourToResponseRate = new TreeMap<>(Comparator.comparingInt(e -> e));
}
