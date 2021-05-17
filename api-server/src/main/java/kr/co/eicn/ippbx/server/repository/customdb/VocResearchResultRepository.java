package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.CommonVocResearchResult;
import kr.co.eicn.ippbx.server.model.entity.customdb.VocResearchResultEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.VocGroupEntity;
import kr.co.eicn.ippbx.server.model.search.CustomDBVOCResultSearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.VocGroup.VOC_GROUP;
import static kr.co.eicn.ippbx.server.jooq.customdb.tables.VocResearchResult.VOC_RESEARCH_RESULT;

@Getter
public class VocResearchResultRepository extends CustomDBBaseRepository<CommonVocResearchResult, VocResearchResultEntity, Integer>{
    protected final Logger logger = LoggerFactory.getLogger(VocResearchResultRepository.class);

    private final CommonVocResearchResult TABLE;

    public VocResearchResultRepository(String table) {
        super(new CommonVocResearchResult(table), new CommonVocResearchResult(table).SEQ, VocResearchResultEntity.class);
        this.TABLE = new CommonVocResearchResult(table);

        addField(TABLE);
        orderByFields.add(TABLE.RESULT_DATE.asc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .leftJoin(VOC_GROUP)
                .on(TABLE.VOC_GROUP_ID.eq(VOC_GROUP.SEQ))
                .where(DSL.noCondition());
    }

    @Override
    protected RecordMapper<Record, VocResearchResultEntity> getMapper() {
        return record -> {
            final VocResearchResultEntity entity = record.into(TABLE).into(VocResearchResultEntity.class);
            entity.setGroup(record.into(VOC_GROUP).into(VocGroupEntity.class));
            return entity;
        };
    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE)
                .columns(VOC_RESEARCH_RESULT.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(VOC_RESEARCH_RESULT.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }

    public Pagination<VocResearchResultEntity> pagination(CustomDBVOCResultSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(CustomDBVOCResultSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));
        if (search.getVocGroupSeq() != null)
            conditions.add(VOC_GROUP.SEQ.eq(search.getVocGroupSeq()));

        return conditions;
    }

}
