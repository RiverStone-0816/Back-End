package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatOutbound;
import kr.co.eicn.ippbx.model.dto.eicn.DashServiceStatResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatOutboundEntity;
import kr.co.eicn.ippbx.model.search.AbstractStatSearchRequest;
import kr.co.eicn.ippbx.model.search.StatOutboundSearchRequest;
import kr.co.eicn.ippbx.model.search.StatTotalSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.util.FunctionUtils;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

@Getter
public class StatOutboundRepository extends StatDBBaseRepository<CommonStatOutbound, StatOutboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatOutboundRepository.class);

    private final CommonStatOutbound TABLE;

    @Autowired
    private ServiceRepository serviceRepository;

    public StatOutboundRepository(String companyId) {
        super(new CommonStatOutbound(companyId), new CommonStatOutbound(companyId).SEQ, StatOutboundEntity.class);
        this.TABLE = new CommonStatOutbound(companyId);

        addField(
                TABLE.SEQ,
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                ifnull(sum(TABLE.TOTAL), 0).as(TABLE.TOTAL),
                ifnull(sum(TABLE.SUCCESS), 0).as(TABLE.SUCCESS),
                ifnull(sum(TABLE.BILLSEC_SUM), 0).as(TABLE.BILLSEC_SUM),
                ifnull(sum(TABLE.WAIT_SUM), 0).as(TABLE.WAIT_SUM));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        setTimeUnit(query);

        return query.where();
    }

    public List<StatOutboundEntity> findAll(StatOutboundSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(StatOutboundSearchRequest search) {
        final List<Condition> conditions = defaultConditions(search);

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

        return conditions;
    }

    public List<StatOutboundEntity> findAllTotal(StatTotalSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(StatTotalSearchRequest search) {

        final List<Condition> conditions = defaultConditions(search);

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

        return conditions;
    }

    public List<Condition> defaultConditions(AbstractStatSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        standardTime = search.getTimeUnit();

        conditions.add(getOutboundCondition(search));

        return conditions;
    }

    public Map<Byte, DashServiceStatResponse> getHourOutInbound() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        return dsl.select(TABLE.STAT_HOUR)
                .select(sum(TABLE.SUCCESS).as("successCnt"))
                .from(TABLE)
                .where(TABLE.STAT_DATE.eq(date(now())))
                .and(TABLE.STAT_HOUR.eq((byte) currentHour)
                             .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 1))))
                             .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 2))))
                             .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 3))))
                             .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 4))))
                             .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 5))))
                )
                .and(TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(TABLE.DCONTEXT.eq("outbound"))
                .groupBy(TABLE.STAT_HOUR)
                .fetchMap(TABLE.STAT_HOUR, DashServiceStatResponse.class);
    }

    public StatOutboundEntity getTodayStat() {
        return dsl.select(ifnull(sum(TABLE.TOTAL), 0).as("total"),
                          ifnull(sum(TABLE.SUCCESS), 0).as("success"))
                .from(TABLE)
                .where(TABLE.STAT_DATE.eq(date(now())))
                .and(TABLE.DCONTEXT.eq("outbound"))
                .fetchOneInto(StatOutboundEntity.class);
    }
}
