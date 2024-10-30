package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatUserInbound;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserInboundEntity;
import kr.co.eicn.ippbx.model.enums.ContextType;
import kr.co.eicn.ippbx.model.search.AbstractStatSearchRequest;
import kr.co.eicn.ippbx.model.search.StatHuntSearchRequest;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
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
import java.util.Map;

import static org.jooq.impl.DSL.*;

@Getter
public class StatUserInboundRepository extends StatDBBaseRepository<CommonStatUserInbound, StatUserInboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatDBBaseRepository.class);

    private final CommonStatUserInbound TABLE;
    private       String                type = "";

    public StatUserInboundRepository(String companyId) {
        super(new CommonStatUserInbound(companyId), new CommonStatUserInbound(companyId).SEQ, StatUserInboundEntity.class);
        this.TABLE = new CommonStatUserInbound(companyId);

        addField(
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                TABLE.GROUP_CODE,
                TABLE.USERID,
                TABLE.SERVICE_NUMBER,
                TABLE.HUNT_NUMBER,
                TABLE.DCONTEXT,
                ifnull(sum(TABLE.IN_TOTAL), 0).as(TABLE.IN_TOTAL),
                ifnull(sum(TABLE.IN_SUCCESS), 0).as(TABLE.IN_SUCCESS),
                ifnull(sum(TABLE.IN_HUNT_NOANSWER), 0).as(TABLE.IN_HUNT_NOANSWER),
                ifnull(sum(TABLE.IN_BILLSEC_SUM), 0).as(TABLE.IN_BILLSEC_SUM),
                ifnull(sum(TABLE.CALLBACK_SUCC), 0).as(TABLE.CALLBACK_SUCC),
                ifnull(sum(TABLE.SERVICE_LEVEL_OK), 0).as(TABLE.SERVICE_LEVEL_OK),
                ifnull(sum(TABLE.IN_WAITSEC_SUM), 0).as(TABLE.IN_WAITSEC_SUM)
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
            query.groupBy(TABLE.USERID);
        }

        setTimeUnit(query);

        return query.where();
    }

    public List<StatUserInboundEntity> findAllUserStat(StatUserSearchRequest search) {
        standardTime = search.getTimeUnit();
        type = "";
        return findAll(userConditions(search));
    }

    public List<StatUserInboundEntity> findAllUserTotalStat(StatUserSearchRequest search) {
        standardTime = null;
        type = "total";
        return findAll(userConditions(search));
    }

    public List<StatUserInboundEntity> findAllHuntStat(StatHuntSearchRequest search, List<QueueName> queueNameList, String searchGroupTreeName) {
        type = "hunt";
        return findAll(huntConditions(search, queueNameList, searchGroupTreeName));
    }

    public List<StatUserInboundEntity> findAllHuntTotalStat(StatHuntSearchRequest search, List<QueueName> queueNameList, String searchGroupTreeName) {
        type = "total";
        return findAll(huntConditions(search, queueNameList, searchGroupTreeName));
    }

    public List<Condition> defaultConditions(AbstractStatSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        standardTime = search.getTimeUnit();

        conditions.add(getInboundCondition(search));

        return conditions;
    }

    public List<Condition> userConditions(StatUserSearchRequest search) {
        final List<Condition> conditions = defaultConditions(search);
        conditions.add(TABLE.USER_STAT_YN.eq("Y"));

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    return conditions;
                case MINE:
                    conditions.add(TABLE.USERID.eq(g.getUser().getId()));
                    break;
                case GROUP:
                    conditions.add(TABLE.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        if (CollectionUtils.isNotEmpty(search.getServiceNumbers()))
            conditions.add(TABLE.SERVICE_NUMBER.in(search.getServiceNumbers()));

        if (StringUtils.isNotEmpty(search.getGroupCode()))
            conditions.add(TABLE.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%"));

        if (CollectionUtils.isNotEmpty(search.getPersonIds()))
            conditions.add(TABLE.USERID.in(search.getPersonIds()));

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
                case MINE:
                    conditions.add(TABLE.USERID.eq(g.getUser().getId()));
                    break;
                case GROUP:
                    conditions.add(TABLE.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        conditions.add(getDefaultCondition("inbound", TABLE.DCONTEXT.eq(ContextType.HUNT_CALL.getCode()), search));

        if (CollectionUtils.isNotEmpty(search.getServiceNumbers()))
            conditions.add(TABLE.SERVICE_NUMBER.in(search.getServiceNumbers()));

        if (CollectionUtils.isNotEmpty(queueNameList))
            conditions.add(TABLE.HUNT_NUMBER.in(queueNameList.stream().map(QueueName::getNumber).toList()));

        if (StringUtils.isNotEmpty(searchGroupTreeName))
            conditions.add(TABLE.GROUP_TREE_NAME.like(searchGroupTreeName + "%"));

        if (CollectionUtils.isNotEmpty(search.getPersonIds()))
            conditions.add(TABLE.USERID.in(search.getPersonIds()));

        return conditions;
    }

    public StatUserInboundEntity getBillsecSumByHunt(String userId, String huntNumber) {
        final StatUserInboundEntity entity = new StatUserInboundEntity();
        final List<StatUserInboundEntity> entityList = findAllByHunt(userId, huntNumber);

        entity.setInSuccess(0);
        entity.setInBillsecSum(0);

        for (StatUserInboundEntity inboundEntity : entityList) {
            entity.setInBillsecSum(entity.getInBillsecSum() + inboundEntity.getInBillsecSum());
            entity.setInSuccess(entity.getInSuccess() + inboundEntity.getInSuccess());
        }

        return entity;
    }

    public List<StatUserInboundEntity> findAllByHunt(String userId, String queueNumber) {
        return dsl.select(TABLE.fields())
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.DCONTEXT.eq("hunt_context"))
                .and(TABLE.USERID.eq(userId))
                .and(TABLE.HUNT_NUMBER.eq(queueNumber))
                .fetchInto(StatUserInboundEntity.class);
    }

    public Map<String, StatUserInboundEntity> findAllUserIndividualStat() {
        return dsl.select(TABLE.USERID,
                          ifnull(sum(TABLE.IN_TOTAL), 0).as(TABLE.IN_TOTAL),
                          ifnull(sum(TABLE.IN_SUCCESS), 0).as(TABLE.IN_SUCCESS),
                          ifnull(sum(TABLE.IN_BILLSEC_SUM), 0).as(TABLE.IN_BILLSEC_SUM))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.DCONTEXT.eq(ContextType.INBOUND.getCode()).or(TABLE.DCONTEXT.eq(ContextType.DIRECT_CALL.getCode())).or(TABLE.DCONTEXT.eq(ContextType.HUNT_CALL.getCode())))
                .groupBy(TABLE.USERID)
                .fetchMap(TABLE.USERID, StatUserInboundEntity.class);
    }

    public StatUserInboundEntity findUserCallStat() {
        return dsl.select(TABLE.USERID,
                          ifnull(sum(TABLE.IN_TOTAL), 0).as(TABLE.IN_TOTAL),
                          ifnull(sum(TABLE.IN_SUCCESS), 0).as(TABLE.IN_SUCCESS),
                          ifnull(sum(TABLE.CALLBACK_SUCC), 0).as(TABLE.CALLBACK_SUCC))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.USERID.eq(g.getUser().getId()))
                .groupBy(TABLE.USERID)
                .fetchOneInto(StatUserInboundEntity.class);
    }

    public Map<String, Object> findChatUserInboundMonitor(List<String> person) {
        return dsl.select(TABLE.USERID
                        , ifnull(sum(TABLE.IN_SUCCESS), 0).as("in_success"))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.USERID.in(person))
                .groupBy(TABLE.USERID)
                .fetchMap(TABLE.USERID, field("in_success"));
    }
}
