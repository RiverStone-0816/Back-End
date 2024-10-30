package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatInbound;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.enums.ContextType;
import kr.co.eicn.ippbx.model.search.AbstractStatSearchRequest;
import kr.co.eicn.ippbx.model.search.StatHuntSearchRequest;
import kr.co.eicn.ippbx.model.search.StatInboundSearchRequest;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
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

import static org.jooq.impl.DSL.*;

@Getter
public class StatInboundForHuntStatRepository extends StatDBBaseRepository<CommonStatInbound, StatInboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatInboundForHuntStatRepository.class);

    private final CommonStatInbound TABLE;
    private       String            type = "";

    // 큐그룹별통계가 stat_user_inbound_cjlogistics 테이블 대신 stat_inbound_cjlogistics 테이블을 사용하기 위한 메서드
    public StatInboundForHuntStatRepository(String companyId) {
        super(new CommonStatInbound(companyId), new CommonStatInbound(companyId).SEQ, StatInboundEntity.class);
        this.TABLE = new CommonStatInbound(companyId);

        final Condition isDirectOrInner = TABLE.DCONTEXT.eq(ContextType.DIRECT_CALL.getCode()).or(TABLE.DCONTEXT.eq(ContextType.INBOUND_INNER.getCode()));

        addField(
                TABLE.HUNT_NUMBER,
                TABLE.SERVICE_NUMBER,
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                ifnull(sum(TABLE.TOTAL), 0).as(TABLE.TOTAL),
                ifnull(sum(TABLE.ONLYREAD), 0).as(TABLE.ONLYREAD),
                ifnull(sum(when(isDirectOrInner, TABLE.TOTAL).else_(TABLE.CONNREQ)), 0).as(TABLE.CONNREQ),
                ifnull(sum(TABLE.SUCCESS), 0).as(TABLE.SUCCESS),
                ifnull(sum(when(isDirectOrInner, TABLE.TOTAL.minus(TABLE.SUCCESS)).else_(TABLE.CANCEL)), 0).as(TABLE.CANCEL),
                ifnull(sum(TABLE.CANCEL_TIMEOUT), 0).as(TABLE.CANCEL_TIMEOUT),
                ifnull(sum(TABLE.CALLBACK), 0).as(TABLE.CALLBACK),
//                ifnull(sum(TABLE.CANCEL_CALLBACK), 0).as(TABLE.CANCEL_CALLBACK),
                ifnull(sum(TABLE.CALLBACK_SUCCESS), 0).as(TABLE.CALLBACK_SUCCESS),
                ifnull(sum(TABLE.SERVICE_LEVEL_OK), 0).as(TABLE.SERVICE_LEVEL_OK),
                ifnull(sum(TABLE.CANCEL_CUSTOM), 0).as(TABLE.CANCEL_CUSTOM),
                ifnull(sum(TABLE.BILLSEC_SUM), 0).as(TABLE.BILLSEC_SUM),
                ifnull(sum(when(not(isDirectOrInner.or(TABLE.DCONTEXT.eq(ContextType.CALL_BACK.getCode()))), TABLE.WAIT_SUM)), 0).as(TABLE.WAIT_SUM),
                ifnull(sum(TABLE.WAIT_SUCC_0_10), 0).as(TABLE.WAIT_SUCC_0_10),
                ifnull(sum(TABLE.WAIT_SUCC_10_20), 0).as(TABLE.WAIT_SUCC_10_20),
                ifnull(sum(TABLE.WAIT_SUCC_20_30), 0).as(TABLE.WAIT_SUCC_20_30),
                ifnull(sum(TABLE.WAIT_SUCC_30_40), 0).as(TABLE.WAIT_SUCC_30_40),
                ifnull(sum(TABLE.WAIT_SUCC_40), 0).as(TABLE.WAIT_SUCC_40),
                ifnull(sum(TABLE.WAIT_CANCEL_0_10), 0).as(TABLE.WAIT_CANCEL_0_10),
                ifnull(sum(TABLE.WAIT_CANCEL_10_20), 0).as(TABLE.WAIT_CANCEL_10_20),
                ifnull(sum(TABLE.WAIT_CANCEL_20_30), 0).as(TABLE.WAIT_CANCEL_20_30),
                ifnull(sum(TABLE.WAIT_CANCEL_30_40), 0).as(TABLE.WAIT_CANCEL_30_40),
                ifnull(sum(TABLE.WAIT_CANCEL_40), 0).as(TABLE.WAIT_CANCEL_40)
        );
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        if (type.equals("hunt")) {
            query.groupBy(TABLE.HUNT_NUMBER);
        } else if (type.equals("total")) {
        } else if (type.equals("groupTotal")) {
            query.groupBy(TABLE.GROUP_CODE);
        } else {
            query.groupBy(TABLE.HUNT_NUMBER);
        }

        setTimeUnit(query);

        return query.where();
    }

    public List<StatInboundEntity> findAll(StatInboundSearchRequest search) {
        return findAll(conditions(search));
    }

    public List<StatInboundEntity> findAllHuntStat(StatHuntSearchRequest search, List<QueueName> queueNameList, String searchGroupTreeName) {
        type = "hunt";
        return findAll(huntConditions(search, queueNameList, searchGroupTreeName));
    }

    public List<StatInboundEntity> findAllHuntTotalStat(StatHuntSearchRequest search, List<QueueName> queueNameList, String searchGroupTreeName) {
        type = "total";
        return findAll(huntConditions(search, queueNameList, searchGroupTreeName));
    }

    public List<Condition> conditions(StatInboundSearchRequest search) {
        final List<Condition> conditions = defaultConditions(search);

        if (CollectionUtils.isNotEmpty(search.getServiceNumbers()))
            conditions.add(TABLE.SERVICE_NUMBER.in(search.getServiceNumbers()));

        if (CollectionUtils.isNotEmpty(search.getQueueNumbers()))
            conditions.add(TABLE.HUNT_NUMBER.in(search.getQueueNumbers()));

        return conditions;
    }

    public List<Condition> defaultConditions(AbstractStatSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        standardTime = search.getTimeUnit();

        conditions.add(getInboundCondition(search));
        return conditions;
    }

    public List<Condition> huntConditions(StatHuntSearchRequest search, List<QueueName> queueNameList, String searchGroupTreeName) {
        final List<Condition> conditions = new ArrayList<>();
        standardTime = search.getTimeUnit();

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    return conditions;
                case GROUP:
                    conditions.add(TABLE.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        conditions.add(getDefaultCondition("inbound", TABLE.DCONTEXT.eq(ContextType.HUNT_CALL.getCode()), search));

        if (CollectionUtils.isNotEmpty(search.getServiceNumbers()))
            conditions.add(TABLE.SERVICE_NUMBER.in(search.getServiceNumbers()));

        if (CollectionUtils.isNotEmpty(queueNameList))
            conditions.add(TABLE.HUNT_NUMBER.in(queueNameList));

        if (StringUtils.isNotEmpty(searchGroupTreeName))
            conditions.add(TABLE.GROUP_TREE_NAME.like(searchGroupTreeName + "%"));

        return conditions;
    }
}
