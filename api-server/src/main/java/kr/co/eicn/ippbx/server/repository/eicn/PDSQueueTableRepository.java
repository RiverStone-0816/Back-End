package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.PdsQueueTable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PdsQueueTable.PDS_QUEUE_TABLE;

@Getter
@Repository
public class PDSQueueTableRepository extends EicnBaseRepository<PdsQueueTable, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PdsQueueTable, String> {
	protected final Logger logger = LoggerFactory.getLogger(PDSQueueTableRepository.class);

	public PDSQueueTableRepository() {
		super(PDS_QUEUE_TABLE, PDS_QUEUE_TABLE.NAME, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PdsQueueTable.class);
	}
}
