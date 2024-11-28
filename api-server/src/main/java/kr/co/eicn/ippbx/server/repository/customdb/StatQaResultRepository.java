package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.model.enums.CallType;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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

    public Map<Integer, List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo>> findAllIndividualResult(StatQaResultSearchRequest search) {
        return dsl.select(TABLE.RESULT_DATE, TABLE.RESULT_TYPE, TABLE.RS_CODE_1, TABLE.RS_CODE_2, TABLE.RS_CODE_3, TABLE.RS_CODE_4, TABLE.RS_CODE_5, TABLE.RS_CODE_6, TABLE.RS_CODE_7, TABLE.RS_CODE_8, TABLE.RS_CODE_9, TABLE.RS_CODE_10)
                .from(TABLE)
                .where(conditions(search))
                .fetchGroups(TABLE.RESULT_TYPE, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
    }

    private List<Condition> conditions(StatQaResultSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(TABLE.GROUP_KIND.ne("PHONE_TMP"));

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate())));

        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (Objects.nonNull(search.getType()))
            conditions.add(TABLE.CALL_TYPE.eq(search.getType().equals(StatQaResultSearchRequest.SendReceiveType.SEND) ? "O" : "I"));

        if (Objects.nonNull(search.getGroupSeq()))
            conditions.add(TABLE.GROUP_ID.eq(search.getGroupSeq()));

        if (Objects.nonNull(search.getResultType()))
            conditions.add(TABLE.RESULT_TYPE.eq(search.getResultType()));

        return conditions;
    }
}
