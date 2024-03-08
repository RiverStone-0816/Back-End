package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatVoc;
import kr.co.eicn.ippbx.model.entity.statdb.StatVOCEntity;
import kr.co.eicn.ippbx.model.search.StatDBVOCStatisticsSearchRequest;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocGroup.VOC_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.statdb.tables.StatVoc.STAT_VOC;

@Getter
public class StatVOCRepository extends StatDBBaseRepository<CommonStatVoc, StatVOCEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatVOCRepository.class);

    private final CommonStatVoc TABLE;

    public StatVOCRepository(String table) {
        super(new CommonStatVoc(table), new CommonStatVoc(table).SEQ, StatVOCEntity.class);
        this.TABLE = new CommonStatVoc(table);
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .leftJoin(VOC_GROUP)
                .on(TABLE.VOC_GROUP_ID.eq(VOC_GROUP.SEQ))
                .where(DSL.noCondition());
    }

    @Override
    protected RecordMapper<Record, StatVOCEntity> getMapper() {
        return record -> {
            final StatVOCEntity entity = record.into(TABLE).into(StatVOCEntity.class);
            entity.setGroup(record.into(VOC_GROUP).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup.class));
            return entity;
        };
    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE)
                .columns(STAT_VOC.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(STAT_VOC.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }

    public List<StatVOCEntity> findAll(StatDBVOCStatisticsSearchRequest search) {
        return super.findAll(conditions(search));
    }

    private List<Condition> conditions(StatDBVOCStatisticsSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(TABLE.STAT_DATE.ge(search.getStartDate()));
        if (search.getEndDate() != null)
            conditions.add(TABLE.STAT_DATE.le(search.getEndDate()));
        if (search.getVocGroupSeq() != null)
            conditions.add(VOC_GROUP.SEQ.eq(search.getVocGroupSeq()));

        return conditions;
    }
}
