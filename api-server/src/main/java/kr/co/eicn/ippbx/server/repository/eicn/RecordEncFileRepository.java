package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordEncFile;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.RecordEncFileEntity;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordEncFile.RECORD_ENC_FILE;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordEncKey.RECORD_ENC_KEY;

@Getter
@Repository
public class RecordEncFileRepository extends EicnBaseRepository<RecordEncFile, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncFile, String> {
	protected final Logger logger = LoggerFactory.getLogger(RecordEncFileRepository.class);
	@Value("${env.enc.master-key}")
	private String ENC_MASTER_KEY;
	private final PBXServerInterface pbxServerInterface;
	private final CacheService cacheService;

	public RecordEncFileRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
		super(RECORD_ENC_FILE, RECORD_ENC_FILE.RECORD_FILE, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncFile.class);
		this.pbxServerInterface = pbxServerInterface;
		this.cacheService = cacheService;
	}

	public RecordEncFileEntity findOneByRecordFile(final String recordFile) {
		return findOneByRecordFile(dsl, recordFile);
	}

	public RecordEncFileEntity findOnePBXByRecordFile(final String recordFile) {
		RecordEncFileEntity encFileEntity = new RecordEncFileEntity();
		final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

		pbxServerList.forEach(e -> {
			try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
				ReflectionUtils.copy(encFileEntity, findOneByRecordFile(pbxDsl, recordFile));
			}
		});
		return encFileEntity;
	}

	public RecordEncFileEntity findOneByRecordFile(DSLContext dslContext, final String recordFile) {
		return dslContext.select(RECORD_ENC_FILE.fields())
				.select(RECORD_ENC_KEY.ID, RECORD_ENC_KEY.CREATE_TIME, RECORD_ENC_KEY.COMPANY_ID)
				.select(DSL.field("AES_DECRYPT(UNHEX({0}),{1})", String.class, RECORD_ENC_KEY.ENC_KEY, ENC_MASTER_KEY).as(RECORD_ENC_KEY.ENC_KEY))
				.from(RECORD_ENC_FILE)
				.join(RECORD_ENC_KEY)
				.on(RECORD_ENC_FILE.KEY_ID.eq(RECORD_ENC_KEY.ID))
				.where(compareCompanyId())
				.and(RECORD_ENC_FILE.RECORD_FILE.eq(recordFile))
				.fetchAny(record -> {
					final RecordEncFileEntity entity = record.into(RECORD_ENC_FILE).into(RecordEncFileEntity.class);
					entity.setRecordEncKey(record.into(RECORD_ENC_KEY).into(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncKey.class));
					return entity;
				});
	}
}
