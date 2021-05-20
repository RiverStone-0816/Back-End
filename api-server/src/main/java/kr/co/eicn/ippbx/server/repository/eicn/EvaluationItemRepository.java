package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationItem;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.EvaluationCategoryRecord;
import kr.co.eicn.ippbx.model.form.EvaluationItemFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationWholeCategoryFormRequest;
import lombok.Getter;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationCategory.EVALUATION_CATEGORY;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationForm.EVALUATION_FORM;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationItem.EVALUATION_ITEM;

@Getter
@Repository
public class EvaluationItemRepository extends EicnBaseRepository<EvaluationItem, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItem, Long> {
    protected final Logger logger = LoggerFactory.getLogger(EvaluationItemRepository.class);

    public EvaluationItemRepository() {
        super(EVALUATION_ITEM, EVALUATION_ITEM.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItem.class);
    }

    public void insert(EvaluationWholeCategoryFormRequest form, final Long evaluationId) {
        final Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm> optionalEvaluationForm = dsl.select().from(EVALUATION_FORM)
                .where(EVALUATION_FORM.COMPANY_ID.eq(getCompanyId())).and(EVALUATION_FORM.ID.eq(evaluationId))
                .fetchOptionalInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm.class);
        if (!optionalEvaluationForm.isPresent())
            throw new EntityNotFoundException("평가지 정보를 찾을 수 없습니다.");

        final int sum = form.getCategories().stream().flatMap(e -> e.getItems().stream()).mapToInt(EvaluationItemFormRequest::getMaxScore).sum();
        if (sum != 100)
            throw new IllegalStateException("평가항목의 배점의 합이 '100'이어야 합니다.");

        dsl.delete(EVALUATION_ITEM)
                .where(EVALUATION_ITEM.CATEGORY.in(dsl.select(EVALUATION_CATEGORY.ID).from(EVALUATION_CATEGORY).where(EVALUATION_CATEGORY.EVALUATION_ID.eq(evaluationId)).fetch()))
                .execute();

        dsl.delete(EVALUATION_CATEGORY)
                .where(EVALUATION_CATEGORY.EVALUATION_ID.eq(evaluationId))
                .and(EVALUATION_CATEGORY.COMPANY_ID.eq(getCompanyId()))
                .execute();

        form.getCategories().forEach(category -> {
            final EvaluationCategoryRecord evaluationCategoryRecord = dsl.insertInto(EVALUATION_CATEGORY)
                    .set(EVALUATION_CATEGORY.NAME, category.getName())
                    .set(EVALUATION_CATEGORY.COMPANY_ID, getCompanyId())
                    .set(EVALUATION_CATEGORY.EVALUATION_ID, evaluationId)
                    .returning()
                    .fetchOne();

            final Long categoryCode = evaluationCategoryRecord.getValue(EVALUATION_CATEGORY.ID);

            final Collection<Query> queries = new ArrayList<>();

            final List<EvaluationItemFormRequest> items = category.getItems();
            for (int i = 0, length = items.size(); i < length; i++) {
                final EvaluationItemFormRequest item = items.get(i);
                queries.add(DSL.insertInto(EVALUATION_ITEM)
                        .set(EVALUATION_ITEM.CATEGORY, categoryCode)
                        .set(EVALUATION_ITEM.NAME, item.getName())
                        .set(EVALUATION_ITEM.VALUATION_BASIS, item.getValuationBasis())
                        .set(EVALUATION_ITEM.MAX_SCORE, item.getMaxScore())
                        .set(EVALUATION_ITEM.REMARK, item.getRemark())
                        .set(EVALUATION_ITEM.SEQUENCE, i)
                );
            }

            dsl.batch(queries).execute();
        });
    }
}
