package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordDown;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.RecordDownRecord;
import kr.co.eicn.ippbx.server.model.UserDetails;
import kr.co.eicn.ippbx.server.model.form.RecordDownFormRequest;
import kr.co.eicn.ippbx.server.model.search.RecordDownSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordDown.RECORD_DOWN;

@Getter
@Repository
public class RecordDownRepository extends EicnBaseRepository<RecordDown, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDown, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(RecordEncRepository.class);

	public RecordDownRepository() {
		super(RECORD_DOWN, RECORD_DOWN.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDown.class);
		orderByFields.add(RECORD_DOWN.REQUESTDATE.desc());
	}

	public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordDown> findAll(RecordDownSearchRequest search) {
		return super.findAll(conditions(search));
	}

	private List<Condition> conditions(RecordDownSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		conditions.add(RECORD_DOWN.REQUESTDATE.greaterOrEqual(Timestamp.valueOf(search.getStartDate() + " 00:00:00")));
		conditions.add(RECORD_DOWN.REQUESTDATE.lessOrEqual(Timestamp.valueOf(search.getEndDate() + " 23:59:59")));

		return conditions;
	}

	public RecordDownRecord insertOnGeneratedKey(RecordDownFormRequest form) {
		final UserDetails requestUser = g.getUser();

		return dsl.insertInto(RECORD_DOWN)
				.set(RECORD_DOWN.DOWN_NAME, form.getDownName())
				.set(RECORD_DOWN.DOWN_FOLDER, requestUser.getId() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
				.set(RECORD_DOWN.USERID, requestUser.getId())
				.set(RECORD_DOWN.USER_NAME, requestUser.getIdName())
				.set(RECORD_DOWN.REQUESTDATE, DSL.now())
				.set(RECORD_DOWN.COMPANY_ID, getCompanyId())
				.returning()
				.fetchOne();
	}
}
