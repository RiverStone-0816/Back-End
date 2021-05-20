package kr.co.eicn.ippbx.server.repository;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTreeLevelName;
import kr.co.eicn.ippbx.model.form.CompanyTreeNameFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EicnBaseRepository;
import lombok.Getter;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTreeLevelName.COMPANY_TREE_LEVEL_NAME;

@Getter
@Repository
public class CompanyTreeNameRepositoryTest extends EicnBaseRepository<CompanyTreeLevelName, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTreeLevelName, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CompanyTreeNameRepositoryTest.class);

	public CompanyTreeNameRepositoryTest() {
		super(COMPANY_TREE_LEVEL_NAME, COMPANY_TREE_LEVEL_NAME.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTreeLevelName.class);

		orderByFields.add(COMPANY_TREE_LEVEL_NAME.GROUP_LEVEL.asc());
	}

	public void createdMetaTypes(Set<CompanyTreeNameFormRequest> records) {
		final Collection<Query> queries = new ArrayList<>();

		for (CompanyTreeNameFormRequest record: records) {
			queries.add(
					DSL.insertInto(COMPANY_TREE_LEVEL_NAME)
							.set(COMPANY_TREE_LEVEL_NAME.GROUP_LEVEL, record.getGroupLevel())
							.set(COMPANY_TREE_LEVEL_NAME.GROUP_TREE_NAME, record.getGroupTreeName())
							.set(COMPANY_TREE_LEVEL_NAME.COMPANY_ID, getCompanyId())
			);
		}

		dsl.batch(queries).execute();
	}
}
