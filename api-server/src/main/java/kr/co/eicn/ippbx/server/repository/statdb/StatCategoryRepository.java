package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.server.jooq.statdb.tables.CommonStatInbound;
import kr.co.eicn.ippbx.server.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;
import kr.co.eicn.ippbx.server.model.search.StatCategorySearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jooq.impl.DSL.sum;

@Getter
public class StatCategoryRepository extends StatDBBaseRepository<CommonStatInbound, StatInboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatCategoryRepository.class);

    private final CommonStatInbound TABLE;

    public StatCategoryRepository(String companyId) {
        super(new CommonStatInbound(companyId), new CommonStatInbound(companyId).SEQ, StatInboundEntity.class);
        this.TABLE = new CommonStatInbound(companyId);

        addField(
                TABLE.SEQ,
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                TABLE.SERVICE_NUMBER,
                TABLE.CATEGORY,
                TABLE.IVR_TREE_NAME,
                sum(TABLE.TOTAL).as(TABLE.TOTAL),
                sum(TABLE.ONLYREAD).as(TABLE.ONLYREAD),
                sum(TABLE.CONNREQ).as(TABLE.CONNREQ),
                sum(TABLE.SUCCESS).as(TABLE.SUCCESS),
                sum(TABLE.CANCEL).as(TABLE.CANCEL),
                sum(TABLE.CALLBACK).as(TABLE.CALLBACK),
                sum(TABLE.CANCEL_CALLBACK).as(TABLE.CANCEL_CALLBACK),
                sum(TABLE.CALLBACK_SUCCESS).as(TABLE.CALLBACK_SUCCESS),
                sum(TABLE.SERVICE_LEVEL_OK).as(TABLE.SERVICE_LEVEL_OK),
                sum(TABLE.CANCEL_CUSTOM).as(TABLE.CANCEL_CUSTOM),
                sum(TABLE.BILLSEC_SUM).as(TABLE.BILLSEC_SUM),
                sum(TABLE.WAIT_SUM).as(TABLE.WAIT_SUM),
                sum(TABLE.WAIT_SUCC_0_10).as(TABLE.WAIT_SUCC_0_10),
                sum(TABLE.WAIT_SUCC_10_20).as(TABLE.WAIT_SUCC_10_20),
                sum(TABLE.WAIT_SUCC_20_30).as(TABLE.WAIT_SUCC_20_30),
                sum(TABLE.WAIT_SUCC_30_40).as(TABLE.WAIT_SUCC_30_40),
                sum(TABLE.WAIT_SUCC_40).as(TABLE.WAIT_SUCC_40),
                sum(TABLE.WAIT_CANCEL_0_10).as(TABLE.WAIT_CANCEL_0_10),
                sum(TABLE.WAIT_CANCEL_10_20).as(TABLE.WAIT_CANCEL_10_20),
                sum(TABLE.WAIT_CANCEL_20_30).as(TABLE.WAIT_CANCEL_20_30),
                sum(TABLE.WAIT_CANCEL_30_40).as(TABLE.WAIT_CANCEL_30_40),
                sum(TABLE.WAIT_CANCEL_40).as(TABLE.WAIT_CANCEL_40));
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
        List<Condition> conditions = conditions(search);

        conditions.add(TABLE.SERVICE_NUMBER.isNotNull().and(TABLE.IVR_TREE_NAME.isNotNull()));

        return findAll(conditions(search));
    }

    public List<Condition> conditions(StatCategorySearchRequest search) {
        List<Condition> conditions = new ArrayList<>();
        Condition serviceCondition = null;
        Condition categoryCondition = null;
        Condition inboundCondition = TABLE.DCONTEXT.eq("inbound").or(TABLE.DCONTEXT.eq("hunt_context"));

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.STAT_DATE.ge(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.STAT_DATE.le(search.getEndDate()));

        if (search.getTimeUnit() != null)
            standardTime = search.getTimeUnit();

        if (search.getServiceNumbers().size() > 0) {
            for (int i = 0; i < search.getServiceNumbers().size(); i++) {
                if (i == 0)
                    serviceCondition = TABLE.SERVICE_NUMBER.eq(search.getServiceNumbers().get(i));
                else
                    serviceCondition = serviceCondition.or(TABLE.SERVICE_NUMBER.eq(search.getServiceNumbers().get(i)));

                conditions.add(serviceCondition);
            }
        }
        if (search.getIvrMulti() != null) {
            for (int i = 0; i < search.getIvrMulti().size(); i++) {
                if (i == 0)
                    categoryCondition = TABLE.IVR_TREE_NAME.eq(search.getIvrMulti().get(i));
                else
                    categoryCondition = categoryCondition.or(TABLE.IVR_TREE_NAME.eq(search.getIvrMulti().get(i)));

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
        if (search.getBusy())
            inboundCondition = inboundCondition.or(TABLE.DCONTEXT.eq("busy_context"));
        if (!search.getBusy())
            inboundCondition = inboundCondition.or(TABLE.DCONTEXT.notEqual("busy_context"));

        conditions.add(inboundCondition);
        return conditions;
    }
}
