package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.server.model.search.StatQaResultIndividualSearchRequest;
import kr.co.eicn.ippbx.server.model.search.StatQaResultSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.jooq.impl.DSL.noCondition;

@Getter
public class StatQaResultRepository extends CustomDBBaseRepository<CommonResultCustomInfo, kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo, Integer> {
    private Logger logger = LoggerFactory.getLogger(StatQaResultRepository.class);

    private final CommonResultCustomInfo TABLE;

    public StatQaResultRepository(String companyId) {
        super(new CommonResultCustomInfo(companyId), new CommonResultCustomInfo(companyId).SEQ, kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
        this.TABLE = new CommonResultCustomInfo(companyId);

        addField(TABLE);
    }

    public List<kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo> findAll(StatQaResultSearchRequest search) {
        return dsl.select(TABLE.fields())
                .from(TABLE)
                .where(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate())))
                .and(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")))
                .and(TABLE.RS_CODE_1.isNotNull())
                .fetchInto(kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
    }

    private List<Condition> conditions(StatQaResultSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate())));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));
//        if (StringUtils.isNotEmpty(search.getType().name())) {
//            if (Objects.equals(SendReceiveType.SEND, search.getType()))
//                conditions.add(TABLE.);
//            else
//                conditions.add(TABLE.);
//        }

        return conditions;
    }

    public Map<Integer, List<kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo>> findAllIndividualResult(StatQaResultIndividualSearchRequest search) {
        Condition condition = noCondition();
        if (Objects.nonNull(search.getStartDate()))
            condition = condition.and(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (Objects.nonNull(search.getStartDate()))
            condition = condition.and(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        return dsl.selectFrom(TABLE)
                .where(condition)
                .fetchGroups(TABLE.RESULT_TYPE, kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
    }
}
