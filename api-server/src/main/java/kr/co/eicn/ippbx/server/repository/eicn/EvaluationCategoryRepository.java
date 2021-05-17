package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationCategory;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.EvaluationCategoryRecord;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationItemEntity;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationCategory.EVALUATION_CATEGORY;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.EvaluationItem.EVALUATION_ITEM;

@Getter
@Repository
public class EvaluationCategoryRepository extends EicnBaseRepository<EvaluationCategory, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationCategory, Long> {
	protected final Logger logger = LoggerFactory.getLogger(EvaluationCategoryRepository.class);

	public EvaluationCategoryRepository() {
		super(EVALUATION_CATEGORY, EVALUATION_CATEGORY.ID, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationCategory.class);
	}

	public List<EvaluationCategoryEntity> getCategories(final Long evaluationId) {
		final Map<EvaluationCategoryRecord, Result<Record>> evaluationCategoryRecordResultMap = dsl.select()
				.from(EVALUATION_CATEGORY)
				.leftJoin(EVALUATION_ITEM)
					.on(EVALUATION_CATEGORY.ID.eq(EVALUATION_ITEM.CATEGORY))
				.where(EVALUATION_CATEGORY.COMPANY_ID.eq(getCompanyId()))
				.and(EVALUATION_CATEGORY.EVALUATION_ID.eq(evaluationId))
				.orderBy(EVALUATION_CATEGORY.ID.asc(), EVALUATION_ITEM.SEQUENCE.asc())
				.fetch()
				.intoGroups(EVALUATION_CATEGORY);

		final List<EvaluationCategoryEntity> entities = new ArrayList<>();

		evaluationCategoryRecordResultMap.forEach((record, records) -> {
			final EvaluationCategoryEntity entity = record.into(EVALUATION_CATEGORY).into(EvaluationCategoryEntity.class);
			entity.setItems(records.stream().filter(r -> r.getValue(EVALUATION_ITEM.ID) != null)
				.map(r -> r.into(EVALUATION_ITEM).into(EvaluationItemEntity.class))
				.collect(Collectors.toList())
			);

			entities.add(entity);
		});

		return entities;
	}
}
