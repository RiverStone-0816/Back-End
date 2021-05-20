package kr.co.eicn.ippbx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.model.dto.eicn.PersonOnHunt;
import kr.co.eicn.ippbx.model.dto.eicn.QueueNameResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitorIvrTree extends IvrTree {
    private List<MonitorIvrTree> nodes;

    private QueueNameResponse queueNameResponse;
    private List<PersonOnHunt> queueMemberList;
    private Integer processingCustomerCount = 0; // 대기사담원수 표시
    private Integer waitingCustomerCount = 0; // 고객대기자수

    @JsonIgnore
    public boolean isLeaf() {
        return nodes == null || nodes.isEmpty();
    }
}
