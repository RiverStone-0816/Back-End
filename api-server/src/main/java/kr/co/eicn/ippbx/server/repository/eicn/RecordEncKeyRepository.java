package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.RecordEncKey;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.RecordEncKeyFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.RecordEncKey.RECORD_ENC_KEY;

@Getter
@Repository
public class RecordEncKeyRepository extends EicnBaseRepository<RecordEncKey, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(RecordEncKeyRepository.class);

	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;
	private final WebSecureHistoryRepository webSecureHistoryRepository;
	@Value("${env.enc.master-key}")
	private String ENC_MASTER_KEY;

	public RecordEncKeyRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, WebSecureHistoryRepository webSecureHistoryRepository) {
		super(RECORD_ENC_KEY, RECORD_ENC_KEY.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey.class);
		orderByFields.add(RECORD_ENC_KEY.CREATE_TIME.desc());

		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
		this.webSecureHistoryRepository = webSecureHistoryRepository;
	}

	@Override
	protected SelectJoinStep<Record> query(DSLContext dsl) {
		return dsl.select(RECORD_ENC_KEY.ID, RECORD_ENC_KEY.CREATE_TIME, RECORD_ENC_KEY.COMPANY_ID)
				.select(DSL.field("AES_DECRYPT(UNHEX({0}),{1})", String.class, RECORD_ENC_KEY.ENC_KEY, ENC_MASTER_KEY).as(RECORD_ENC_KEY.ENC_KEY))
				.from(table);
	}

	public Record insertOnGeneratedKey(RecordEncKeyFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey entity = findOne(RECORD_ENC_KEY.CREATE_TIME.eq(Timestamp.valueOf(form.getApplyDate())));
		if (entity != null)
			throw new DuplicateKeyException("동일한 암호키 적용시간이 이미 존재합니다.");

		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey();
		record.setCreateTime(Timestamp.valueOf(form.getApplyDate()));
		record.setEncKey(form.getEncKey());

		final Record r = this.insertOnGeneratedKey(record);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.insert(pbxDsl, record);
					}
				});


		webSecureHistoryRepository.insert(WebSecureActionType.RECORD_ENC, WebSecureActionSubType.SAVE_ENC_KEY, "");

		return r;
	}

	public void updateByKey(RecordEncKeyFormRequest form, Integer id) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record = findOneIfNullThrow(id);
		// 적용시간이 지난 암호키는 수정할 수 없다.
//		if (record.getCreateTime().toLocalDateTime().isBefore(LocalDateTime.now()))
//			throw new IllegalArgumentException("현재 사용 중이거나 이미 사용된 KEY는 변경할 수 없습니다.");

		record.setEncKey(form.getEncKey());
		record.setCreateTime(Timestamp.valueOf(form.getApplyDate()));

		this.updateByKey(record, id);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						this.updateByKey(pbxDsl, record, id);
					}
				});

		webSecureHistoryRepository.insert(WebSecureActionType.RECORD_ENC, WebSecureActionSubType.SAVE_ENC_KEY, "");
	}

	public void deleteByKey(Integer id) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record = findOneIfNullThrow(id);
		// 적용시간이 지난 암호키는 삭제할 수 없다.
//		if (record.getCreateTime().toLocalDateTime().isBefore(LocalDateTime.now()))
//			throw new IllegalArgumentException("현재 사용 중인 KEY는 삭제할 수 없습니다.");

		delete(id);

		cacheService.pbxServerList(getCompanyId())
				.forEach(e -> {
					try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
						super.delete(pbxDsl, id);
					}
				});
		webSecureHistoryRepository.insert(WebSecureActionType.RECORD_ENC, WebSecureActionSubType.DEL, "");
	}

	public Record insertOnGeneratedKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record) {
		return dsl.insertInto(RECORD_ENC_KEY)
				.set(RECORD_ENC_KEY.COMPANY_ID, getCompanyId())
				.set(RECORD_ENC_KEY.CREATE_TIME, record.getCreateTime())
				.set(RECORD_ENC_KEY.ENC_KEY, DSL.field("HEX(AES_ENCRYPT({0},{1}))", String.class, record.getEncKey(), ENC_MASTER_KEY))
				.returning()
				.fetchOne();
	}

	public void updateByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record, Integer key) {
		dsl.update(table)
				.set(RECORD_ENC_KEY.CREATE_TIME, record.getCreateTime())
				.set(RECORD_ENC_KEY.ENC_KEY, DSL.field("HEX(AES_ENCRYPT({0},{1}))", String.class, record.getEncKey(), ENC_MASTER_KEY))
				.where(compareCompanyId())
				.and(getCondition(key))
				.execute();
	}

	public void insert(DSLContext pbxDsl, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record) {
		pbxDsl.insertInto(RECORD_ENC_KEY)
				.set(RECORD_ENC_KEY.COMPANY_ID, getCompanyId())
				.set(RECORD_ENC_KEY.CREATE_TIME, record.getCreateTime())
				.set(RECORD_ENC_KEY.ENC_KEY, DSL.field("HEX(AES_ENCRYPT({0},{1}))", String.class, record.getEncKey(), ENC_MASTER_KEY))
				.returning()
				.fetchOne();
	}

	public void updateByKey(DSLContext pbxDsl, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordEncKey record, Integer key) {
		pbxDsl.update(table)
				.set(RECORD_ENC_KEY.CREATE_TIME, record.getCreateTime())
				.set(RECORD_ENC_KEY.ENC_KEY, DSL.field("HEX(AES_ENCRYPT({0},{1}))", String.class, record.getEncKey(), ENC_MASTER_KEY))
				.where(compareCompanyId())
				.and(getCondition(key))
				.execute();
	}
}
