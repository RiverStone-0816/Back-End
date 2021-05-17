package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueueEntity extends QueueName {
	private QueueTable queueTable;
	private QueueName subGroupQueueName;
}
