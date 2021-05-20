package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTreeLevelName;
import kr.co.eicn.ippbx.model.form.CompanyTreeNameFormRequest;
import kr.co.eicn.ippbx.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.CompanyTreeLevelNameSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTreeLevelName.COMPANY_TREE_LEVEL_NAME;

@Getter
@Repository
public class CompanyTreeLevelNameRepository extends EicnBaseRepository<CompanyTreeLevelName, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTreeLevelName, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CompanyTreeLevelNameRepository.class);

	public CompanyTreeLevelNameRepository() {
		super(COMPANY_TREE_LEVEL_NAME, COMPANY_TREE_LEVEL_NAME.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTreeLevelName.class);

		orderByFields.add(COMPANY_TREE_LEVEL_NAME.GROUP_LEVEL.asc());
	}

	public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTreeLevelName> listMetaType() {
		return super.findAll();
	}

	public void createdMetaTypes(Set<CompanyTreeNameFormRequest> records) {
		final Collection<Query> queries = new ArrayList<>();

		for (CompanyTreeNameFormRequest record: records) {
			queries.add(
					DSL.insertInto(COMPANY_TREE_LEVEL_NAME)
					.set(COMPANY_TREE_LEVEL_NAME.GROUP_LEVEL, record.getGroupLevel())
					.set(COMPANY_TREE_LEVEL_NAME.GROUP_TREE_NAME, record.getGroupTreeName())
					.set(COMPANY_TREE_LEVEL_NAME.COMPANY_ID, g.getUser().getCompanyId())
			);
		}

		dsl.batch(queries).execute();
	}

	public void updateMetaTypes(Set<CompanyTreeNameUpdateFormRequest> records) {
		final Collection<Query> queries = new ArrayList<>();

		for (CompanyTreeNameUpdateFormRequest record: records) {
			queries.add(
					DSL.update(COMPANY_TREE_LEVEL_NAME)
							.set(dsl.newRecord(COMPANY_TREE_LEVEL_NAME, record))
							.where(COMPANY_TREE_LEVEL_NAME.GROUP_LEVEL.eq(record.getGroupLevel()))
					.and(COMPANY_TREE_LEVEL_NAME.COMPANY_ID.eq(getCompanyId()))
			);
		}

		dsl.batch(queries).execute();
	}

	private List<Condition> conditions(CompanyTreeLevelNameSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		return conditions;
	}
}
