package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationForm;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationCategory;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.server.model.form.EvaluationFormRequest;
import kr.co.eicn.ippbx.server.model.form.EvaluationFormVisibleRequest;
import kr.co.eicn.ippbx.server.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationCategory.EVALUATION_CATEGORY;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationForm.EVALUATION_FORM;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationItem.EVALUATION_ITEM;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationResult.EVALUATION_RESULT;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class EvaluationFormRepository extends EicnBaseRepository<EvaluationForm, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm, Long> {
    protected final Logger logger = LoggerFactory.getLogger(EvaluationFormRepository.class);

    private final EvaluationCategoryRepository evaluationCategoryRepository;

    public EvaluationFormRepository(EvaluationCategoryRepository evaluationCategoryRepository) {
        super(EVALUATION_FORM, EVALUATION_FORM.ID, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm.class);
        this.evaluationCategoryRepository = evaluationCategoryRepository;
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm> pagination(PageForm search) {
        return super.pagination(search);
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm> findAll(EvaluationFormSearchRequest search) {
        return super.findAll(conditions(search));
    }

    public EvaluationFormEntity get(final Long id) {
        final EvaluationFormEntity entity = modelMapper.map(findOneIfNullThrow(id), EvaluationFormEntity.class);
        entity.setCategories(evaluationCategoryRepository.getCategories(id));

        return entity;
    }

    public Record insertOnGeneratedKey(EvaluationFormRequest form) {
        return dsl.insertInto(EVALUATION_FORM)
                .set(EVALUATION_FORM.NAME, form.getName())
                .set(EVALUATION_FORM.START_DATE, form.getStartDate())
                .set(EVALUATION_FORM.END_DATE, form.getEndDate())
                .set(EVALUATION_FORM.USE_TYPE, form.getUseType())
                .set(EVALUATION_FORM.INSERT_ID, g.getUser().getId())
                .set(EVALUATION_FORM.MEMO, form.getMemo())
                .set(EVALUATION_FORM.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne();
    }

    public void updateByKey(EvaluationFormRequest form, Long id) {
        dsl.update(EVALUATION_FORM)
                .set(EVALUATION_FORM.NAME, form.getName())
                .set(EVALUATION_FORM.USE_TYPE, form.getUseType())
                .set(EVALUATION_FORM.START_DATE, form.getStartDate())
                .set(EVALUATION_FORM.END_DATE, form.getEndDate())
                .set(EVALUATION_FORM.MEMO, form.getMemo())
                .where(EVALUATION_FORM.ID.eq(id))
                .execute();
    }

    public void hiddenUpdate(final List<EvaluationFormVisibleRequest> ids) {
        final Map<Long, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm> entityMap = findAll().stream().collect(Collectors.toMap(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationForm::getId, e -> e));

        dsl.batch(ids.stream().filter(form -> entityMap.get(form.getId()) != null)
                .map(form -> DSL.update(EVALUATION_FORM).set(EVALUATION_FORM.HIDDEN, form.getHidden()).where(EVALUATION_FORM.ID.eq(form.getId())))
                .collect(Collectors.toList()))
                .execute();
    }

    public int delete(Long id) {
        dsl.delete(EVALUATION_RESULT)
                .where(EVALUATION_RESULT.COMPANY_ID.eq(getCompanyId()))
                .and(EVALUATION_RESULT.EVALUATION_ID.eq(id))
                .execute();

        return super.delete(id);
    }

    public void copy(EvaluationFormRequest form, final Long sourceId) {
        findOneIfNullThrow(sourceId);
        final Long evaluationId = this.insertOnGeneratedKey(form).getValue(EVALUATION_FORM.ID);
        final List<EvaluationCategory> categoryList = evaluationCategoryRepository.findAll(EVALUATION_CATEGORY.EVALUATION_ID.eq(sourceId));
        if (categoryList.size() > 0) {
            for (EvaluationCategory category : categoryList) {
                dsl.insertInto(EVALUATION_ITEM)
                        .columns(EVALUATION_ITEM.NAME, EVALUATION_ITEM.CATEGORY, EVALUATION_ITEM.VALUATION_BASIS, EVALUATION_ITEM.MAX_SCORE, EVALUATION_ITEM.REMARK)
                        .select(dsl.select(EVALUATION_ITEM.NAME
                                , DSL.value(dsl.insertInto(EVALUATION_CATEGORY).columns(EVALUATION_CATEGORY.EVALUATION_ID, EVALUATION_CATEGORY.NAME, EVALUATION_CATEGORY.COMPANY_ID)
                                        .select(dsl.select(DSL.value(evaluationId), EVALUATION_CATEGORY.NAME, EVALUATION_CATEGORY.COMPANY_ID)
                                                .from(EVALUATION_CATEGORY)
                                                .where(EVALUATION_CATEGORY.EVALUATION_ID.eq(sourceId)
                                                .and(EVALUATION_CATEGORY.ID.eq(category.getId())))
                                        )
                                        .returning()
                                        .fetchOne().getValue(EVALUATION_CATEGORY.ID)
                                )
                                , EVALUATION_ITEM.VALUATION_BASIS
                                , EVALUATION_ITEM.MAX_SCORE
                                , EVALUATION_ITEM.REMARK)
                                .from(EVALUATION_ITEM)
                                .where(EVALUATION_ITEM.CATEGORY.eq(category.getId())
                                )
                        )
                        .execute();
            }
        }
    }

    private List<Condition> conditions(EvaluationFormSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (isNotEmpty(search.getName()))
            conditions.add(EVALUATION_FORM.NAME.like("%" + search.getName() + "%"));
        if (search.getUseTypes() != null && !search.getUseTypes().isEmpty())
            conditions.add(EVALUATION_FORM.USE_TYPE.in(search.getUseTypes()));
        if (search.getVisible() != null)
            conditions.add(EVALUATION_FORM.HIDDEN.eq(search.getVisible() ? "N" : "Y"));
        if (search.getStartDate() != null)
            conditions.add(EVALUATION_FORM.START_DATE.isNull().or(EVALUATION_FORM.START_DATE.le(search.getStartDate())));
        if (search.getEndDate() != null)
            conditions.add(EVALUATION_FORM.END_DATE.isNull().or(EVALUATION_FORM.END_DATE.ge(search.getEndDate())));

        return conditions;
    }

    public void hide(Long id) {
        dsl.update(EVALUATION_FORM)
                .set(EVALUATION_FORM.HIDDEN, "Y")
                .where(EVALUATION_FORM.ID.eq(id))
                .execute();
    }

    public void show(Long id) {
        dsl.update(EVALUATION_FORM)
                .set(EVALUATION_FORM.HIDDEN, "N")
                .where(EVALUATION_FORM.ID.eq(id))
                .execute();
    }
}
