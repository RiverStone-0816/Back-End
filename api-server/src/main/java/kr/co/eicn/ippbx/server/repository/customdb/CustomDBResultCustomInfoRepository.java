package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.ResultCustomInfo;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomDBResultCustomInfoRepository extends CustomDBBaseRepository<CommonResultCustomInfo, ResultCustomInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CustomDBResultCustomInfoRepository.class);

	private final CommonResultCustomInfo TABLE;
	private final Table<Record> VIEW;
	private final String RESULT_CST_BY_RESULT = "view_maindb_result_by_maindb";

	public CustomDBResultCustomInfoRepository(String table) {
		super(new CommonResultCustomInfo(table), new CommonResultCustomInfo(table).SEQ, ResultCustomInfo.class);
		this.TABLE = new CommonResultCustomInfo(table);
		this.VIEW = DSL.table(RESULT_CST_BY_RESULT + "_" + getCompanyId());
	}

	public List<ResultCustomInfoEntity> viewResultCustomInfo() {
		return dsl.select()
				.from(VIEW)
				.fetchInto(ResultCustomInfoEntity.class);
	}

	private List<Condition> viewConditions(Object search) {
		final List<Condition> conditions = new ArrayList<>();

		return conditions;
	}
}
