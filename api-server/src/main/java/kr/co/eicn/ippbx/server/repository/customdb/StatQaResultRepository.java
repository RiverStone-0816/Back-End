package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.model.enums.CallType;
import kr.co.eicn.ippbx.model.search.StatQaResultIndividualSearchRequest;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.jooq.impl.DSL.noCondition;

@Getter
public class StatQaResultRepository extends CustomDBBaseRepository<CommonResultCustomInfo, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo, Integer> {
    private final CommonResultCustomInfo TABLE;
    private Logger logger = LoggerFactory.getLogger(StatQaResultRepository.class);

    public StatQaResultRepository(String companyId) {
        super(new CommonResultCustomInfo(companyId), new CommonResultCustomInfo(companyId).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
        this.TABLE = new CommonResultCustomInfo(companyId);

        addField(TABLE);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo> findAll(StatQaResultSearchRequest search) {
        return dsl.select(TABLE.fields())
                .from(TABLE)
                .where(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate())))
                .and(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")))
                .and(TABLE.RS_CODE_1.isNotNull())
                .and(ObjectUtils.isNotEmpty(search.getType()) ? TABLE.CALL_TYPE.eq(search.getType().equals(StatQaResultSearchRequest.SendReceiveType.SEND) ? CallType.OUTBOUND.getCode() : CallType.INBOUND.getCode()) : DSL.trueCondition())
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
    }

    public Map<Integer, List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo>> findAllIndividualResult(StatQaResultIndividualSearchRequest search) {
        Condition condition = noCondition();
        if (Objects.nonNull(search.getStartDate()))
            condition = condition.and(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (Objects.nonNull(search.getStartDate()))
            condition = condition.and(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));
        if (ObjectUtils.isNotEmpty(search.getType())) {
            if (Objects.equals(StatQaResultSearchRequest.SendReceiveType.SEND, search.getType()))
                condition = condition.and(TABLE.CALL_TYPE.eq(CallType.OUTBOUND.getCode()));
            else
                condition = condition.and(TABLE.CALL_TYPE.eq(CallType.INBOUND.getCode()));
        }

        return dsl.selectFrom(TABLE)
                .where(condition)
                .fetchGroups(TABLE.RESULT_TYPE, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
    }
}
