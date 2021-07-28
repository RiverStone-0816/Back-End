package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonVocResult;
import kr.co.eicn.ippbx.model.entity.customdb.VocResearchResultEntity;
import kr.co.eicn.ippbx.model.entity.eicn.VocGroupEntity;
import kr.co.eicn.ippbx.model.search.CustomDBVOCResultSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocGroup.VOC_GROUP;

@Getter
public class CustomDBVOCResultRepository extends CustomDBBaseRepository<CommonVocResult, VocResearchResultEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CustomDBVOCResultRepository.class);

    private final CommonVocResult TABLE;

    public CustomDBVOCResultRepository(String table) {
        super(new CommonVocResult(table), new CommonVocResult(table).SEQ, VocResearchResultEntity.class);
        this.TABLE = new CommonVocResult(table);
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

    public Pagination<VocResearchResultEntity> pagination(CustomDBVOCResultSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(CustomDBVOCResultSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()) && Objects.nonNull(search.getEndDate())) {
            conditions.add(DSL.date(TABLE.RESULT_DATE).ge(search.getStartDate()));
            conditions.add(DSL.date(TABLE.RESULT_DATE).le(search.getEndDate()));

            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        }
        if (search.getVocGroupSeq() != null)
            conditions.add(VOC_GROUP.SEQ.eq(search.getVocGroupSeq()));

        return conditions;
    }
}
