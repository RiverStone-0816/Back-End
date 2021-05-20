package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ResearchList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.ResearchListRecord;
import kr.co.eicn.ippbx.model.form.ResearchListFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchListSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.InsertSetMoreStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ResearchList.RESEARCH_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ResearchTree.RESEARCH_TREE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ResearchListRepository extends EicnBaseRepository<ResearchList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList, Integer> {
    private final Logger logger = LoggerFactory.getLogger(ResearchListRepository.class);

    private final CompanyTreeRepository companyTreeRepository;

    public ResearchListRepository(CompanyTreeRepository companyTreeRepository) {
        super(RESEARCH_LIST, RESEARCH_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList.class);
        this.companyTreeRepository = companyTreeRepository;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList> pagination(ResearchListSearchRequest search) {
        return super.pagination(search, conditions(search), Arrays.asList(RESEARCH_LIST.RESEARCH_ID.asc()));
    }

    private List<Condition> conditions(ResearchListSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(RESEARCH_LIST.COMPANY_ID.eq(getCompanyId()));
        if (isNotEmpty(search.getResearchName()))
            conditions.add(RESEARCH_LIST.RESEARCH_NAME.like("%" + search.getResearchName() + "%"));

        return conditions;
    }

    public void insert(ResearchListFormRequest form) {
        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList> duplicatedResearchName = findAll(RESEARCH_LIST.RESEARCH_NAME.eq(form.getResearchName())).stream().findAny();
        if (duplicatedResearchName.isPresent())
            throw new DuplicateKeyException("중복된 설문명이 있습니다.");

        final InsertSetMoreStep<ResearchListRecord> query = dsl.insertInto(RESEARCH_LIST)
                .set(RESEARCH_LIST.RESEARCH_ID, nextSequence())
                .set(RESEARCH_LIST.RESEARCH_NAME, form.getResearchName())
                .set(RESEARCH_LIST.REGDATE, DSL.now())
                .set(RESEARCH_LIST.COMPANY_ID, getCompanyId());

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null ) {
                query.set(RESEARCH_LIST.GROUP_CODE, companyTree.getGroupCode())
                        .set(RESEARCH_LIST.GROUP_TREE_NAME, companyTree.getGroupTreeName())
                        .set(RESEARCH_LIST.GROUP_LEVEL, companyTree.getGroupLevel());
            }
        }

        query.execute();
    }

    public void updateByResearchId(ResearchListFormRequest form, Integer researchId) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList entity = findOneIfNullThrow(RESEARCH_LIST.RESEARCH_ID.eq(researchId));

        entity.setResearchName(form.getResearchName());

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                entity.setGroupCode(form.getGroupCode());
                entity.setGroupTreeName(companyTree.getGroupTreeName());
                entity.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        dsl.update(RESEARCH_LIST)
                .set(dsl.newRecord(RESEARCH_LIST, entity))
                .where(RESEARCH_LIST.RESEARCH_ID.eq(researchId))
                .and(RESEARCH_LIST.COMPANY_ID.eq(getCompanyId()))
                .execute();
    }

    public int delete(Integer researchId) {
        findOneIfNullThrow(RESEARCH_LIST.RESEARCH_ID.eq(researchId));

        final int deleteRow = super.delete(RESEARCH_LIST.RESEARCH_ID.eq(researchId));

        dsl.deleteFrom(RESEARCH_TREE)
                .where(RESEARCH_TREE.COMPANY_ID.eq(getCompanyId()))
                .and(RESEARCH_TREE.RESEARCH_ID.eq(researchId))
                .execute();

        return deleteRow;
    }

    private Integer nextSequence() {
        final ResearchList sequenceSeed = RESEARCH_LIST.as("SEQUENCE_SEED");
        return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.RESEARCH_ID), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList findOneByResearchIdIfNullThrow(Integer researchId){
        return findOneIfNullThrow(RESEARCH_LIST.RESEARCH_ID.eq(researchId));
    }
}
