package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.CompanyTreeRecord;
import kr.co.eicn.ippbx.model.entity.eicn.MonitControlEntity;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.model.entity.eicn.PersonEntity;
import kr.co.eicn.ippbx.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTree.COMPANY_TREE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static org.apache.commons.lang3.StringUtils.*;

@Getter
@Repository
public class CompanyTreeRepository extends EicnBaseRepository<CompanyTree, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CompanyTreeRepository.class);

	public CompanyTreeRepository() {
		super(COMPANY_TREE, COMPANY_TREE.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree.class);

		orderByFields.add(COMPANY_TREE.GROUP_NAME.asc());
	}

	public List<Organization> getAllOrganization() {
		return dsl.select(COMPANY_TREE.fields())
				.from(COMPANY_TREE)
				.where(compareCompanyId())
				.orderBy(COMPANY_TREE.GROUP_NAME.asc(), COMPANY_TREE.GROUP_TREE_NAME.asc(), COMPANY_TREE.PARENT_TREE_NAME.asc())
				.fetchInto(Organization.class);
	}

	public List<MonitControlEntity> getAllOrganizationByBranch(MonitControlSearchRequest search) {
		return dsl.select(COMPANY_TREE.GROUP_CODE,COMPANY_TREE.GROUP_NAME)
				.from(COMPANY_TREE)
				.where(compareCompanyId())
				.and(isNotEmpty(search.getGroupCode()) ? COMPANY_TREE.GROUP_CODE.eq(search.getGroupCode()) : DSL.trueCondition())
				.orderBy(COMPANY_TREE.GROUP_CODE.asc())
				.fetch(record ->{
					final MonitControlEntity entity = record.into(COMPANY_TREE).into(MonitControlEntity.class);
					entity.setPerson(
							dsl.selectDistinct(PERSON_LIST.ID, PERSON_LIST.ID_NAME, PERSON_LIST.IS_LOGIN, PERSON_LIST.PEER, PERSON_LIST.EXTENSION, QUEUE_MEMBER_TABLE.PAUSED)
								.from(PERSON_LIST)
								.join(QUEUE_MEMBER_TABLE).on(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(PERSON_LIST.PEER).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(EMPTY)))
								.where(PERSON_LIST.COMPANY_ID.eq(getCompanyId()))
								.and(PERSON_LIST.ID_TYPE.notEqual("J"))
								.and(PERSON_LIST.GROUP_CODE.eq(entity.getGroupCode()))
								.orderBy(PERSON_LIST.ID_NAME.asc())
								.fetchInto(PersonEntity.class)
					);

					return entity;
				});
	}

	public Record insertOnGeneratedKey(OrganizationFormRequest record) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyTree sequenceTable = COMPANY_TREE.as("GROUP_CODE_SEQUENCE");
		final Integer nextGeneratedKey = dsl.select(DSL.ifnull(DSL.max(sequenceTable.GROUP_CODE), 0).add(1)).from(sequenceTable)
				.where(sequenceTable.COMPANY_ID.eq(getCompanyId())).fetchOneInto(Integer.class);
		final CompanyTreeRecord companyTreeRecord = dsl.newRecord(COMPANY_TREE, record);
		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree> parentTrees = new ArrayList<>();

		companyTreeRecord.setCompanyId(g.getUser().getCompanyId());
		companyTreeRecord.setGroupCode(generateKey(nextGeneratedKey));

		if (isEmpty(record.getParentGroupCode())) {
			companyTreeRecord.setGroupTreeName(companyTreeRecord.getGroupCode());
		} else {
			this.findOneIfNullThrow(COMPANY_TREE.GROUP_CODE.eq(record.getParentGroupCode()));
			factorialParentOrganization(findAll(), record.getParentGroupCode(), parentTrees);

			final String parentTreeName = parentTrees.stream()
					.map(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree::getGroupCode)
					.sorted()
					.collect(Collectors.joining("_"));

			companyTreeRecord.setParentTreeName(parentTreeName);
			companyTreeRecord.setGroupTreeName(parentTreeName.concat("_").concat(companyTreeRecord.getGroupCode()));
		}

		return dsl.insertInto(table)
				.set(companyTreeRecord)
				.returning()
				.fetchOne();
	}

	private String generateKey(Integer sequenceKey) {
		final StringBuilder key = new StringBuilder();
		try {
			key.append(new DecimalFormat("0000").format(sequenceKey));
			return key.toString();
		} catch (Exception e) {
			logger.error("Exception!", e);
		}
		return null;
	}

	public void factorialParentOrganization(List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree> organizations, String parentCode, List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree> parentTreeNames) {
		final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree> any = organizations.stream()
				.filter(e -> e.getGroupCode().equals(parentCode)).findAny();

		any.ifPresent(e -> {
			parentTreeNames.add(e);
			factorialParentOrganization(organizations, e.getParentGroupCode(), parentTreeNames);
		});
	}

	public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree findOneGroupCodeIfNullThrow(String groupCode) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree entity = super.findOne(COMPANY_TREE.GROUP_CODE.eq(groupCode));

		if (entity == null)
			throw new EntityNotFoundException(message.getText("messages.company-tree.notfound"));

		return entity;
	}

	public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree findOneByGroupCode(String groupCode) {
		return super.findOne(COMPANY_TREE.GROUP_CODE.eq(groupCode));
	}
}
