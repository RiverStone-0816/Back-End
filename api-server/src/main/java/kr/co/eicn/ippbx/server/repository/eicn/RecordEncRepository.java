package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.RecordEnc;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.RecordEncFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.RecordEnc.RECORD_ENC;

@Getter
@Repository
public class RecordEncRepository extends EicnBaseRepository<RecordEnc, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEnc, String> {
	protected final Logger logger = LoggerFactory.getLogger(RecordEncRepository.class);

	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;
	private final WebSecureHistoryRepository webSecureHistoryRepository;

	public RecordEncRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, WebSecureHistoryRepository webSecureHistoryRepository) {
		super(RECORD_ENC, RECORD_ENC.COMPANY_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEnc.class);

		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
		this.webSecureHistoryRepository = webSecureHistoryRepository;
	}

	public Integer nextKeyId() {
		final RecordEnc recordEnc = RECORD_ENC.as("KEY_ID");
		return dsl.select(DSL.ifnull(DSL.max(recordEnc.KEY_ID), 0).add(1)).from(recordEnc).where(recordEnc.COMPANY_ID.eq(getCompanyId())).fetchOneInto(Integer.class);
	}

	public void insert(RecordEncFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEnc entity = findAll().stream()
				.findFirst()
				.orElse(new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEnc());

		entity.setCompanyId(getCompanyId());
		entity.setEnable(form.getEncType());
		entity.setExternPlayer("N");

		dsl.insertInto(RECORD_ENC)
				.set(dsl.newRecord(RECORD_ENC, entity))
				.set(RECORD_ENC.KEY_ID, nextKeyId())
				.onDuplicateKeyUpdate()
				.set(dsl.newRecord(RECORD_ENC, entity))
				.execute();

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
					pbxDsl.insertInto(RECORD_ENC)
							.set(pbxDsl.newRecord(RECORD_ENC, entity))
							.set(RECORD_ENC.KEY_ID, nextKeyId())
							.onDuplicateKeyUpdate()
							.set(pbxDsl.newRecord(RECORD_ENC, entity))
							.execute();
				});

		webSecureHistoryRepository.insert(WebSecureActionType.RECORD_ENC, WebSecureActionSubType.ENABLE_ENC, "");
	}
}
