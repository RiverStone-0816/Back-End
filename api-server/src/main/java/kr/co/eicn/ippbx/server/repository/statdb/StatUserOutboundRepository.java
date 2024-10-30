package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatUserOutbound;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserOutboundEntity;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.util.FunctionUtils;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

@Getter
public class StatUserOutboundRepository extends StatDBBaseRepository<CommonStatUserOutbound, StatUserOutboundEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(StatUserOutboundRepository.class);

    private final CommonStatUserOutbound TABLE;

    private boolean isTotal = false;

    @Autowired
    private ServiceRepository serviceRepository;

    public StatUserOutboundRepository(String companyId) {
        super(new CommonStatUserOutbound(companyId), new CommonStatUserOutbound(companyId).SEQ, StatUserOutboundEntity.class);
        TABLE = new CommonStatUserOutbound(companyId);

        addField(
                year(TABLE.STAT_DATE),
                month(TABLE.STAT_DATE),
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                dayOfWeek(TABLE.STAT_DATE),
                TABLE.GROUP_CODE,
                TABLE.USERID,
                ifnull(sum(TABLE.OUT_TOTAL), 0).as(TABLE.OUT_TOTAL),
                ifnull(sum(TABLE.OUT_SUCCESS), 0).as(TABLE.OUT_SUCCESS),
                ifnull(sum(TABLE.OUT_BILLSEC_SUM), 0).as(TABLE.OUT_BILLSEC_SUM),
                ifnull(sum(TABLE.CALLBACK_CALL_CNT), 0).as(TABLE.CALLBACK_CALL_CNT),
                ifnull(sum(TABLE.CALLBACK_CALL_SUCC), 0).as(TABLE.CALLBACK_CALL_SUCC),
                ifnull(sum(TABLE.RESERVE_CALL_CNT), 0).as(TABLE.RESERVE_CALL_CNT),
                ifnull(sum(TABLE.RESERVE_CALL_SUCC), 0).as(TABLE.RESERVE_CALL_SUCC),
                TABLE.DCONTEXT
        );
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        if (!isTotal)
            query.groupBy(TABLE.USERID);

        setTimeUnit(query);

        return query.where();
    }

    public List<StatUserOutboundEntity> findAll(StatUserSearchRequest search) {
        isTotal = false;
        return findAll(conditions(search));
    }

    public List<StatUserOutboundEntity> findAllTotal(StatUserSearchRequest search) {
        standardTime = null;
        isTotal = true;
        return findAll(conditions(search));
    }

    public List<Condition> conditions(StatUserSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        standardTime = search.getTimeUnit();

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

        if (StringUtils.isNotEmpty(search.getGroupCode()))
            conditions.add(TABLE.GROUP_TREE_NAME.like("%" + search.getGroupCode() + "%"));

        if (CollectionUtils.isNotEmpty(search.getPersonIds()))
            conditions.add(TABLE.USERID.in(search.getPersonIds()));

        if (CollectionUtils.isNotEmpty(search.getServiceNumbers())) {
            final Map<String, ServiceList> serviceListMap = serviceRepository.findAll().stream().filter(FunctionUtils.distinctByKey(ServiceList::getSvcNumber)).collect(Collectors.toMap(ServiceList::getSvcNumber, e -> e));
            Condition serviceCondition = DSL.noCondition();

            for (String serviceNumber : search.getServiceNumbers()) {
                if (serviceListMap.containsKey(serviceNumber)) {
                    final ServiceList searchTargetService = serviceListMap.get(serviceNumber);
                    serviceCondition = serviceCondition.or(TABLE.CID_NUMBER.in(searchTargetService.getSvcNumber(), searchTargetService.getSvcCid()));
                } else
                    serviceCondition = serviceCondition.or(TABLE.CID_NUMBER.eq(serviceNumber));
            }

            conditions.add(serviceCondition);
        }

        conditions.add(getOutboundCondition(search));

        return conditions;
    }

    public StatUserOutboundEntity getBillsecSumByHunt(String userId) {
        final StatUserOutboundEntity entity = new StatUserOutboundEntity();
        final List<StatUserOutboundEntity> entityList = findAllByHunt(userId);

        entity.setOutSuccess(0);
        entity.setOutBillsecSum(0);

        for (StatUserOutboundEntity outboundEntity : entityList) {
            entity.setOutBillsecSum(entity.getOutBillsecSum() + outboundEntity.getOutBillsecSum());
            entity.setOutSuccess(entity.getOutSuccess() + outboundEntity.getOutSuccess());
        }

        return entity;
    }

    public List<StatUserOutboundEntity> findAllByHunt(String userId) {
        return dsl.select(TABLE.fields())
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.DCONTEXT.eq("hunt_context"))
                .and(TABLE.USERID.eq(userId))
                .fetchInto(StatUserOutboundEntity.class);
    }

    public Map<String, StatUserOutboundEntity> findAllUserIndividualStat() {
        return dsl.select(TABLE.USERID,
                          ifnull(sum(TABLE.OUT_TOTAL), 0).as(TABLE.OUT_TOTAL),
                          ifnull(sum(TABLE.OUT_SUCCESS), 0).as(TABLE.OUT_SUCCESS),
                          ifnull(sum(TABLE.OUT_BILLSEC_SUM), 0).as(TABLE.OUT_BILLSEC_SUM))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .groupBy(TABLE.USERID)
                .fetchMap(TABLE.USERID, StatUserOutboundEntity.class);
    }

    public StatUserOutboundEntity findUserCallStat() {
        return dsl.select(TABLE.USERID,
                          ifnull(sum(TABLE.OUT_TOTAL), 0).as(TABLE.OUT_TOTAL),
                          ifnull(sum(TABLE.OUT_SUCCESS), 0).as(TABLE.OUT_SUCCESS),
                          ifnull(sum(TABLE.CALLBACK_CALL_SUCC), 0).as(TABLE.CALLBACK_CALL_SUCC))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.USERID.eq(g.getUser().getId()))
                .groupBy(TABLE.USERID)
                .fetchOneInto(StatUserOutboundEntity.class);
    }

    public Map<String, Object> findChatUserOutboundMonitor(List<String> person) {
        return dsl.select(TABLE.USERID
                        , ifnull(sum(TABLE.OUT_SUCCESS), 0).as("out_success"))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.USERID.in(person))
                .groupBy(TABLE.USERID)
                .fetchMap(TABLE.USERID, field("out_success"));
    }
}
