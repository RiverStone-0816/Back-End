package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueTable;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueTable.QUEUE_TABLE;

@Getter
@Repository
public class QueueTableRepository extends EicnBaseRepository<QueueTable, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueTable, String> {
	protected final Logger logger = LoggerFactory.getLogger(QueueTableRepository.class);
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	public QueueTableRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(QUEUE_TABLE, QUEUE_TABLE.NAME, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueTable.class);
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public void deleteQueueTable(String queueName) {
		deleteQueueTable(dsl, queueName);
	}

	public void deleteQueueTable(DSLContext dslContext, String queueName) {
		dslContext.deleteFrom(QUEUE_TABLE)
				.where(compareCompanyId(QUEUE_TABLE))
				.and(QUEUE_TABLE.NAME.eq(queueName))
				.execute();
	}

	@Override
	public void insert(DSLContext dslContext, Object record) {
		dslContext.insertInto(QUEUE_TABLE)
				.set(dslContext.newRecord(QUEUE_TABLE, record))

				.set(QUEUE_TABLE.MONITOR_FORMAT, "wav")
				.set(QUEUE_TABLE.QUEUE_YOUARENEXT, "ars/queue-youarenext")
				.set(QUEUE_TABLE.QUEUE_THEREARE, "ars/queue-thereare")
				.set(QUEUE_TABLE.QUEUE_CALLSWAITING, "ars/queue-callswaiting")
				.set(QUEUE_TABLE.QUEUE_HOLDTIME, "ars/queue-holdtime")
				.set(QUEUE_TABLE.QUEUE_MINUTES, "ars/queue-minutes")
				.set(QUEUE_TABLE.QUEUE_SECONDS, "ars/queue-seconds")
				.set(QUEUE_TABLE.QUEUE_LESSTHAN, "ars/queue-lessthan")
				.set(QUEUE_TABLE.QUEUE_THANKYOU, "ars/queue-thankyou")
				.set(QUEUE_TABLE.QUEUE_REPORTHOLD, "ars/queue-reporthold")
				.execute();
	}

	public void insertAllPbxServers(Object record) {
		this.insert(dsl, record);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.insert(pbxDsl, record);
					}
				});
	}

	public int deleteAllPbxServers(String key) {
		return this.deleteAllPbxServers(dsl, key);
	}

	public int deleteAllPbxServers(DSLContext dslContext, String key) {
		final int r = super.delete(dslContext, key);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						super.delete(pbxDsl, key);
					}
				});

		return r;
	}
}
