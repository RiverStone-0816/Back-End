package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryQueueResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class QueueSummaryAndMemberPeers extends SummaryQueueResponse {
    private Integer waitingCustomerCount = 0;
    private Set<String> peers = new HashSet<>();

    public QueueSummaryAndMemberPeers(SummaryQueueResponse e) {
        ReflectionUtils.copy(this, e);
    }
}
