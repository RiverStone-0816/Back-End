package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatInbound;
import kr.co.eicn.ippbx.model.entity.eicn.ServiceNumberIvrRootEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.enums.ContextType;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatCategorySearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jooq.impl.DSL.*;
import static org.jooq.tools.StringUtils.EMPTY;

@Getter
public class StatCategoryRepository extends StatDBBaseRepository<CommonStatInbound, StatInboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatCategoryRepository.class);

    private final CommonStatInbound TABLE;

    public StatCategoryRepository(String companyId) {
        super(new CommonStatInbound(companyId), new CommonStatInbound(companyId).SEQ, StatInboundEntity.class);
        this.TABLE = new CommonStatInbound(companyId);

        final Condition isDirectOrInner = TABLE.DCONTEXT.eq(ContextType.DIRECT_CALL.getCode()).or(TABLE.DCONTEXT.eq(ContextType.INBOUND_INNER.getCode()));

        addField(
                TABLE.SEQ,
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                TABLE.SERVICE_NUMBER,
                TABLE.CATEGORY,
                TABLE.IVR_TREE_NAME,
                ifnull(sum(TABLE.TOTAL), 0).as(TABLE.TOTAL),
                ifnull(sum(TABLE.ONLYREAD), 0).as(TABLE.ONLYREAD),
                ifnull(sum(when(isDirectOrInner, TABLE.TOTAL).else_(TABLE.CONNREQ)), 0).as(TABLE.CONNREQ),
                ifnull(sum(TABLE.SUCCESS), 0).as(TABLE.SUCCESS),
                ifnull(sum(when(isDirectOrInner, TABLE.TOTAL.minus(TABLE.SUCCESS)).else_(TABLE.CANCEL)), 0).as(TABLE.CANCEL),
                ifnull(sum(TABLE.CANCEL_TIMEOUT), 0).as(TABLE.CANCEL_TIMEOUT),
                ifnull(sum(TABLE.CANCEL_NOANSWER), 0).as(TABLE.CANCEL_NOANSWER),
                ifnull(sum(TABLE.CANCEL_CUSTOM), 0).as(TABLE.CANCEL_CUSTOM),
                ifnull(sum(TABLE.CALLBACK), 0).as(TABLE.CALLBACK),
                ifnull(sum(TABLE.CALLBACK_SUCCESS), 0).as(TABLE.CALLBACK_SUCCESS),
                ifnull(sum(TABLE.SERVICE_LEVEL_OK), 0).as(TABLE.SERVICE_LEVEL_OK),
                ifnull(sum(TABLE.BILLSEC_SUM), 0).as(TABLE.BILLSEC_SUM),
                ifnull(sum(when(not(isDirectOrInner.or(TABLE.DCONTEXT.eq(ContextType.CALL_BACK.getCode()))), TABLE.WAIT_SUM)), 0).as(TABLE.WAIT_SUM),
                ifnull(sum(when(not(isDirectOrInner.or(TABLE.DCONTEXT.eq(ContextType.CALL_BACK.getCode()))), TABLE.WAIT_CANCEL_SUM)), 0).as(TABLE.WAIT_CANCEL_SUM),
                ifnull(sum(TABLE.WAIT_SUCC_0_10), 0).as(TABLE.WAIT_SUCC_0_10),
                ifnull(sum(TABLE.WAIT_SUCC_10_20), 0).as(TABLE.WAIT_SUCC_10_20),
                ifnull(sum(TABLE.WAIT_SUCC_20_30), 0).as(TABLE.WAIT_SUCC_20_30),
                ifnull(sum(TABLE.WAIT_SUCC_30_40), 0).as(TABLE.WAIT_SUCC_30_40),
                ifnull(sum(TABLE.WAIT_SUCC_40), 0).as(TABLE.WAIT_SUCC_40),
                ifnull(sum(TABLE.WAIT_CANCEL_0_10), 0).as(TABLE.WAIT_CANCEL_0_10),
                ifnull(sum(TABLE.WAIT_CANCEL_10_20), 0).as(TABLE.WAIT_CANCEL_10_20),
                ifnull(sum(TABLE.WAIT_CANCEL_20_30), 0).as(TABLE.WAIT_CANCEL_20_30),
                ifnull(sum(TABLE.WAIT_CANCEL_30_40), 0).as(TABLE.WAIT_CANCEL_30_40),
                ifnull(sum(TABLE.WAIT_CANCEL_40), 0).as(TABLE.WAIT_CANCEL_40));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        query.groupBy(TABLE.SEQ);

        if (SearchCycle.DATE.getCode().equals(standardTime.getCode()))
            query.groupBy(TABLE.STAT_DATE);
        else if (SearchCycle.HOUR.getCode().equals(standardTime.getCode()))
            query.groupBy(TABLE.STAT_HOUR);

        return query.where();
    }

    public List<StatInboundEntity> findAll(StatCategorySearchRequest search) {
        return findAll(conditions(search));
    }

    public List<StatInboundEntity> findAllCategoryStat(StatCategorySearchRequest search) {
        final List<Condition> conditions = conditions(search);
        conditions.add(TABLE.SERVICE_NUMBER.isNotNull().and(TABLE.SERVICE_NUMBER.ne(StringUtils.EMPTY)).and(TABLE.IVR_TREE_NAME.isNotNull()).and(TABLE.IVR_TREE_NAME.ne(StringUtils.EMPTY)));

        return findAll(conditions(search));
    }

    public List<Condition> conditions(StatCategorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        Condition categoryCondition = null;
        Condition inboundCondition = TABLE.DCONTEXT.in("inbound", "hunt_context");

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.STAT_DATE.ge(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.STAT_DATE.le(search.getEndDate()));

        if (search.getTimeUnit() != null)
            standardTime = search.getTimeUnit();

        if (!search.getServiceNumbers().isEmpty())
            conditions.add(TABLE.SERVICE_NUMBER.in(search.getServiceNumbers()));

        if (!search.getIvrMulti().isEmpty()) {
            for (int i = 0; i < search.getIvrMulti().size(); i++) {
                if (i == 0)
                    categoryCondition = TABLE.IVR_TREE_NAME.like(search.getIvrMulti().get(i) + "%");
                else
                    categoryCondition = categoryCondition.or(TABLE.IVR_TREE_NAME.like(search.getIvrMulti().get(i) + "%"));

                conditions.add(categoryCondition);
            }
        }

        if (search.getPerson())
            inboundCondition = inboundCondition.or(TABLE.DCONTEXT.eq("pers_context"));
        if (!search.getPerson())
            inboundCondition = inboundCondition.or(TABLE.DCONTEXT.notEqual("pers_context"));
        if (search.getInner())
            inboundCondition = inboundCondition.or(TABLE.DCONTEXT.eq("inbound_inner"));
        if (!search.getInner())
            inboundCondition = inboundCondition.or(TABLE.DCONTEXT.notEqual("inbound_inner"));

        conditions.add(inboundCondition);
        return conditions;
    }

    public List<ServiceNumberIvrRootEntity> getStatNumberIvrRoot(StatCategorySearchRequest search) {
        return dsl.selectDistinct(TABLE.SERVICE_NUMBER.as("number"), DSL.substring(TABLE.IVR_TREE_NAME, 1, 4).as("ivr_root"))
                .from(TABLE)
                .where(conditions(search))
                .and(TABLE.SERVICE_NUMBER.isNotNull().and(TABLE.SERVICE_NUMBER.ne(EMPTY)).and(TABLE.IVR_TREE_NAME.isNotNull()).and(TABLE.IVR_TREE_NAME.ne(EMPTY)))
                .orderBy(DSL.field("null"))
                .fetchInto(ServiceNumberIvrRootEntity.class);
    }
}
