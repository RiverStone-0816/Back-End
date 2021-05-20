package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MohList;
import kr.co.eicn.ippbx.model.search.MohListSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MohList.MOH_LIST;

@Getter
@Repository
public class MohListRepository extends EicnBaseRepository<MohList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList, String> {
	private final Logger logger = LoggerFactory.getLogger(MohListRepository.class);

	public MohListRepository() {
		super(MOH_LIST, MOH_LIST.CATEGORY, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList.class);
		orderByFields.add(MOH_LIST.CATEGORY.desc());
	}

	public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList> pagination(MohListSearchRequest search) {
		return super.pagination(search, conditions(search));
	}

	public Integer nextSequence() {
		final MohList sequenceSeed = MOH_LIST.as("SEQUENCE_SEED");
		return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.COMPANY_SEQ), 0).add(1)).from(MOH_LIST.as("SEQUENCE_SEED")).fetchOneInto(Integer.class);
	}

	private List<Condition> conditions(MohListSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (StringUtils.isNotEmpty(search.getMohName()))
			conditions.add(MOH_LIST.MOH_NAME.like("%" + search.getMohName() + "%"));

		return conditions;
	}
}
