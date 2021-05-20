package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class StatMemberStatusResponse {
    private Integer postProcess = 0;
    private Long postProcessTime = 0L;
    private Map<Integer, Long> statusCountMap = new LinkedHashMap<>();

    @JsonIgnore
    public Long getPostPrecessAvgTime() {
        return postProcess == 0 ? 0 : postProcessTime / postProcess;
    }
}
