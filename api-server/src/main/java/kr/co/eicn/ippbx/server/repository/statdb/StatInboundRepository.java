package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatInbound;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.statdb.CenterStatResponse;
import kr.co.eicn.ippbx.model.dto.statdb.TotalStatResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.enums.ContextType;
import kr.co.eicn.ippbx.model.search.AbstractStatSearchRequest;
import kr.co.eicn.ippbx.model.search.StatInboundSearchRequest;
import kr.co.eicn.ippbx.model.search.StatTotalSearchRequest;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.QUEUE_NAME;
import static org.jooq.impl.DSL.*;

@Getter
public class StatInboundRepository extends StatDBBaseRepository<CommonStatInbound, StatInboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatInboundRepository.class);

    private final CommonStatInbound TABLE;

    public StatInboundRepository(String companyId) {
        super(new CommonStatInbound(companyId), new CommonStatInbound(companyId).SEQ, StatInboundEntity.class);
        this.TABLE = new CommonStatInbound(companyId);

        final Condition isDirectOrInner = TABLE.DCONTEXT.eq(ContextType.DIRECT_CALL.getCode()).or(TABLE.DCONTEXT.eq(ContextType.INBOUND_INNER.getCode()));

        addField(
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                ifnull(sum(TABLE.TOTAL), 0).as(TABLE.TOTAL),
                ifnull(sum(TABLE.ONLYREAD), 0).as(TABLE.ONLYREAD),
                ifnull(sum(when(isDirectOrInner, TABLE.TOTAL).else_(TABLE.CONNREQ)), 0).as(TABLE.CONNREQ),
                ifnull(sum(TABLE.SUCCESS), 0).as(TABLE.SUCCESS),
                ifnull(sum(when(isDirectOrInner, TABLE.TOTAL.minus(TABLE.SUCCESS)).else_(TABLE.CANCEL)), 0).as(TABLE.CANCEL),
                ifnull(sum(TABLE.CANCEL_TIMEOUT), 0).as(TABLE.CANCEL_TIMEOUT),
                ifnull(sum(TABLE.CALLBACK), 0).as(TABLE.CALLBACK),
                ifnull(sum(TABLE.CANCEL_CALLBACK), 0).as(TABLE.CANCEL_CALLBACK),
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
        setTimeUnit(query);

        return query.where();
    }

    public List<StatInboundEntity> findAll(StatInboundSearchRequest search) {
        return findAll(conditions(search));
    }

    public List<StatInboundEntity> findAllTotal(StatTotalSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(StatTotalSearchRequest search) {
        List<Condition> conditions = defaultConditions(search);

        if (search.getServiceNumbers().size() > 0) {
            Condition serviceCondition = DSL.noCondition();
            for (int i = 0; i < search.getServiceNumbers().size(); i++) {
                if (StringUtils.isNotEmpty(search.getServiceNumbers().get(i))) {
                    if (i == 0)
                        serviceCondition = TABLE.SERVICE_NUMBER.eq(search.getServiceNumbers().get(i));
                    else
                        serviceCondition = serviceCondition.or(TABLE.SERVICE_NUMBER.eq(search.getServiceNumbers().get(i)));
                } else
                    break;
            }
            conditions.add(serviceCondition);
        }

        return conditions;
    }

    public List<Condition> conditions(StatInboundSearchRequest search) {
        List<Condition> conditions = defaultConditions(search);

        if (search.getServiceNumbers().size() > 0) {
            Condition serviceCondition = DSL.noCondition();
            for (int i = 0; i < search.getServiceNumbers().size(); i++) {
                if (StringUtils.isNotEmpty(search.getServiceNumbers().get(i))) {
                    if (i == 0)
                        serviceCondition = TABLE.SERVICE_NUMBER.eq(search.getServiceNumbers().get(i));
                    else
                        serviceCondition = serviceCondition.or(TABLE.SERVICE_NUMBER.eq(search.getServiceNumbers().get(i)));
                } else
                    break;
            }
            conditions.add(serviceCondition);
        }

        if (search.getQueueNumbers() != null) {
            Condition huntCondition = DSL.noCondition();
            for (int i = 0; i < search.getQueueNumbers().size(); i++) {
                if (StringUtils.isNotEmpty(search.getQueueNumbers().get(i))) {
                    if (i == 0)
                        huntCondition = TABLE.HUNT_NUMBER.eq(search.getQueueNumbers().get(i));
                    else
                        huntCondition = huntCondition.or(TABLE.HUNT_NUMBER.eq(search.getQueueNumbers().get(i)));
                } else
                    break;
            }
            conditions.add(huntCondition);
        }

        return conditions;
    }

    public List<Condition> defaultConditions(AbstractStatSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        standardTime = search.getTimeUnit();

        conditions.add(getInboundCondition(search));
        return conditions;
    }

    public Map<String, MonitorMajorStatusResponse> findAllByHunt() {
        return dsl.select(QUEUE_NAME.NUMBER,
                        ifnull(sum(TABLE.TOTAL), 0).as(TABLE.TOTAL),
                        ifnull(sum(TABLE.SUCCESS), 0).as(TABLE.SUCCESS),
                        ifnull(sum(TABLE.CONNREQ), 0).as(TABLE.CONNREQ),
                        ifnull(sum(TABLE.ONLYREAD), 0).as(TABLE.ONLYREAD),
                        ifnull(sum(TABLE.CANCEL), 0).as(TABLE.CANCEL),
                        ifnull(sum(TABLE.CALLBACK), 0).as(TABLE.CALLBACK),
                        ifnull(sum(TABLE.CALLBACK_SUCCESS), 0).as(TABLE.CALLBACK_SUCCESS))
                .from(QUEUE_NAME)
                .leftOuterJoin(TABLE)
                .on(QUEUE_NAME.NUMBER.eq(TABLE.HUNT_NUMBER))
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .where(QUEUE_NAME.COMPANY_ID.eq(getCompanyId()))
                .groupBy(QUEUE_NAME.NUMBER)
                .fetchMap(QUEUE_NAME.NUMBER, MonitorMajorStatusResponse.class);
    }

    public DashServiceStatResponse getDashServiceStat(String svcNumber, String huntNumber) {
        DashServiceStatResponse res = null;
        res = dsl.select(DSL.ifnull(DSL.sum(TABLE.TOTAL), 0).as("totalCnt"))
                .select(DSL.ifnull(DSL.sum(TABLE.CONNREQ), 0).as("connReqCnt"))
                .select(DSL.ifnull(DSL.sum(TABLE.SUCCESS), 0).as("successCnt"))
                .select(DSL.ifnull(DSL.sum(TABLE.SERVICE_LEVEL_OK), 0).as("serviceLevelCnt"))
                .from(TABLE)
                .where(TABLE.STAT_DATE.eq(DSL.date(DSL.now())))
                .and(svcNumber.equals("") ? DSL.noCondition() : TABLE.SERVICE_NUMBER.eq(svcNumber))
                .and(huntNumber.equals("") ? DSL.noCondition() : TABLE.HUNT_NUMBER.eq(huntNumber))
                .and(TABLE.DCONTEXT.eq("inbound").or(TABLE.DCONTEXT.eq("hunt_context")).or(TABLE.DCONTEXT.eq("busy_context")))
                .fetchOneInto(DashServiceStatResponse.class);

        res.setRateValue(EicnUtils.getRateValue(res.getSuccessCnt(), res.getConnReqCnt()));
        res.setServiceLevelRateValue(EicnUtils.getRateValue(res.getServiceLevelCnt(), res.getConnReqCnt()));

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        Map<Byte, DashServiceStatResponse> graphData = dsl.select(sum(TABLE.SUCCESS).as("successCnt"))
                .select(sum(TABLE.CONNREQ).as("connReqCnt"))
                .select(TABLE.STAT_HOUR)
                .from(TABLE)
                .where(svcNumber.equals("") ? noCondition() : TABLE.SERVICE_NUMBER.eq(svcNumber))
                .and(huntNumber.equals("") ? noCondition() : TABLE.HUNT_NUMBER.eq(huntNumber))
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.STAT_HOUR.eq((byte) currentHour)
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 1))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 2))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 3))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 4))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 5))))
                )
                .and(TABLE.DCONTEXT.eq("inbound").or(TABLE.DCONTEXT.eq("hunt_context")).or(TABLE.DCONTEXT.eq("busy_context")))
                .and(TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .groupBy(TABLE.STAT_HOUR)
                .fetchMap(TABLE.STAT_HOUR, DashServiceStatResponse.class);

        for (int i = 6; i > 0; i--) {
            byte hour = (byte) (currentHour - i + 1);
            float ave = 0;
            if (graphData.get(hour) != null) {
                DashServiceStatResponse data = graphData.get(hour);

                if (data.getConnReqCnt() == null) {
                    data.setConnReqCnt(0);
                }
                if (data.getSuccessCnt() == null) {
                    data.setSuccessCnt(0);
                }

                ave = EicnUtils.getRateValue(data.getSuccessCnt(), data.getConnReqCnt());
            }
            res.getHourToResponseRate().put(currentHour - i + 1, ave);
        }

        return res;
    }

    public DashCurrentCustomWaitResponse getDashCurrentWait(String svcNumber, String huntNumber) {
        DashCurrentCustomWaitResponse res = null;
        res = dsl.select(ifnull(DSL.sum(TABLE.WAIT_SUM), 0).as("waitSecTotal"))
                .select(ifnull(DSL.sum(TABLE.CONNREQ), 0).as("connReqCnt"))
                .select(ifnull(DSL.sum(TABLE.CANCEL_CUSTOM), 0).as("cancelCnt"))
                .select(ifnull(DSL.max(TABLE.WAIT_MAX), 0).as("waitSecMax"))
                .from(TABLE)
                .where(TABLE.STAT_DATE.eq(DSL.date(DSL.now())))
                .and(svcNumber.equals("") ? DSL.noCondition() : TABLE.SERVICE_NUMBER.eq(svcNumber))
                .and(huntNumber.equals("") ? DSL.noCondition() : TABLE.HUNT_NUMBER.eq(huntNumber))
                .and(TABLE.DCONTEXT.eq("hunt_context")/*.or(TABLE.DCONTEXT.eq("inbound"))*/)
                .fetchOneInto(DashCurrentCustomWaitResponse.class);

        res.setTitle("실시간 고객대기");
        res.setCurrentWaitCnt(0);
        if (res.getWaitSecTotal() == 0 || res.getConnReqCnt() == 0) {
            res.setWaitSecAve(0);
        } else {
            res.setWaitSecAve(res.getWaitSecTotal() / res.getConnReqCnt());
        }
        if (res.getCancelCnt() == 0 || res.getConnReqCnt() == 0) {
            res.setCustomCancelRateValue(0f);
        } else {
            res.setCustomCancelRateValue((float) ((res.getCancelCnt() * 100) / res.getConnReqCnt()));
        }

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        Map<Byte, DashCurrentCustomWaitResponse> graphData = dsl.select(TABLE.STAT_HOUR)
                .select(DSL.sum(TABLE.WAIT_SUM).as("waitSecTotal"))
                .select(DSL.sum(TABLE.CONNREQ).as("connReqCnt"))
                .from(TABLE)
                .where(svcNumber.equals("") ? DSL.noCondition() : TABLE.SERVICE_NUMBER.eq(svcNumber))
                .and(huntNumber.equals("") ? DSL.noCondition() : TABLE.HUNT_NUMBER.eq(huntNumber))
                .and(TABLE.STAT_DATE.eq(DSL.date(DSL.now())))
                .and(TABLE.STAT_HOUR.eq((byte) currentHour)
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 1))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 2))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 3))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 4))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 5))))
                )
                .and(TABLE.COMPANY_ID.eq(getCompanyId()))
                .and(TABLE.DCONTEXT.eq("hunt_context")/*.or(TABLE.DCONTEXT.eq("inbound"))*/)
                .groupBy(TABLE.STAT_HOUR)
                .fetchMap(TABLE.STAT_HOUR, DashCurrentCustomWaitResponse.class);

        for (int i = 6; i > 0; i--) {
            byte hour = (byte) (currentHour - i + 1);
            float ave = 0;
            if (graphData.get(hour) != null) {
                DashCurrentCustomWaitResponse data = graphData.get(hour);

                if (data.getWaitSecTotal() == 0 || data.getConnReqCnt() == 0) {
                    ave = 0;
                } else {
                    ave = (float) data.getWaitSecTotal() / data.getConnReqCnt();
                }
            }
            res.getHourToAvgWaitingTime().put(currentHour - i + 1, ave);
        }

        return res;
    }

    public TotalStatResponse getTodayStat() {
        final Condition isService = TABLE.DCONTEXT.eq(ContextType.INBOUND.getCode()).or(TABLE.DCONTEXT.eq(ContextType.HUNT_CALL.getCode())).or(TABLE.DCONTEXT.eq(ContextType.CALL_BACK.getCode()));
        final Condition isDirect = TABLE.DCONTEXT.eq(ContextType.DIRECT_CALL.getCode());
        final Condition isCallback = TABLE.DCONTEXT.eq(ContextType.CALL_BACK.getCode());

        return dsl.select(ifnull(sum(when(isService, TABLE.TOTAL)), 0).as("serviceTotal"),
                        ifnull(sum(when(isDirect, TABLE.TOTAL)), 0).as("directTotal"),
                        ifnull(sum(when(isService, TABLE.ONLYREAD)), 0).as("viewCount"),
                        ifnull(sum(when(isService, TABLE.CONNREQ)), 0).as("serviceConnectionRequest"),
                        ifnull(sum(when(isService, TABLE.SUCCESS)), 0).as("serviceSuccess"),
                        ifnull(sum(when(isDirect, TABLE.SUCCESS)), 0).as("directSuccess"),
                        ifnull(sum(when(isService, TABLE.CANCEL)), 0).as("serviceCancel"),
                        ifnull(sum(when(isDirect, TABLE.CANCEL)), 0).as("directCancel"),
                        ifnull(sum(TABLE.CALLBACK_SUCCESS), 0).as(TABLE.CALLBACK_SUCCESS))
                .from(TABLE)
                .where(TABLE.STAT_DATE.eq(date(now())))
                .fetchOneInto(TotalStatResponse.class);
    }


    public List<StatInboundEntity> getHourStatGraph() {
        return getHourStatGraph("", "");
    }

    public List<StatInboundEntity> getHourStatGraph(String queueAName, String queueBName) {
        return dsl.select(
                        TABLE.HUNT_NUMBER,
                        TABLE.STAT_HOUR,
                        sum(TABLE.SUCCESS).as("success")
                ).from(TABLE)
                .join(QUEUE_NAME).on(TABLE.HUNT_NUMBER.eq(QUEUE_NAME.NUMBER))
                .where(TABLE.STAT_DATE.eq(date(now())))
                .and((StringUtils.isNotEmpty(queueAName) && StringUtils.isNotEmpty(queueBName)) ? QUEUE_NAME.NAME.eq(queueAName).or(QUEUE_NAME.NAME.eq(queueBName)) : noCondition())
                .groupBy(TABLE.HUNT_NUMBER, TABLE.STAT_HOUR)
                .fetchInto(StatInboundEntity.class);
    }

    public List<StatInboundEntity> getWeeklyStatGraph(String queueAName, String queueBName) {
        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        return dsl.select(
                        TABLE.HUNT_NUMBER,
                        TABLE.STAT_HOUR,
                        sum(TABLE.SUCCESS).as("success")
                ).from(TABLE)
                .join(QUEUE_NAME).on(TABLE.HUNT_NUMBER.eq(QUEUE_NAME.NUMBER))
                .where(TABLE.STAT_DATE.ge(new Date(instance.getTimeInMillis())))
                .and(QUEUE_NAME.NAME.eq(queueAName).or(QUEUE_NAME.NAME.eq(queueBName)))
                .groupBy(TABLE.HUNT_NUMBER, TABLE.STAT_HOUR)
                .fetchInto(StatInboundEntity.class);
    }

    public List<StatInboundEntity> groupingByQueues() {
        return dsl.select(TABLE.HUNT_NUMBER)
                .select(getSelectingFields())
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(DSL.date(DSL.now())))
                .groupBy(TABLE.HUNT_NUMBER)
                .fetchInto(StatInboundEntity.class);
    }

    public List<StatInboundEntity> groupingByServices() {
        final List<StatInboundEntity> statInboundEntities = dsl.select(TABLE.SERVICE_NUMBER)
                .select(getSelectingFields())
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(date(now())))
                .and(TABLE.DCONTEXT.eq("inbound").or(TABLE.DCONTEXT.eq("hunt_context")))
                .groupBy(TABLE.SERVICE_NUMBER)
                .fetchInto(StatInboundEntity.class);

        final List<StatInboundEntity> callBackInboundEntities = dsl.select(TABLE.SERVICE_NUMBER)
                .select(getSelectingFields())
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(date(now())))
                .and(TABLE.DCONTEXT.eq("busy_context"))
                .groupBy(TABLE.SERVICE_NUMBER)
                .fetchInto(StatInboundEntity.class);

        if (callBackInboundEntities.size() > 0) {
            for (StatInboundEntity callback : callBackInboundEntities) {
                final Optional<StatInboundEntity> any = statInboundEntities.stream().filter(e -> e.getServiceNumber().equals(callback.getServiceNumber())).findAny();
                if (any.isPresent()) {
                    final StatInboundEntity e = any.get();
                    e.setTotal(e.getTotal() + callback.getTotal());
                    e.setCancel(e.getCancel() + callback.getCancel());
                    e.setConnreq(e.getConnreq() + callback.getConnreq());
                    e.setCallbackSuccess(callback.getSuccess());
                }
            }
        }

        return statInboundEntities;
    }

    public CenterStatResponse getCenterMonitoring() {
        return dsl.select(ifnull(sum(TABLE.SUCCESS), 0).as("success"),
                        ifnull(sum(when(TABLE.DCONTEXT.eq(ContextType.DIRECT_CALL.getCode()), TABLE.TOTAL).else_(TABLE.CONNREQ)), 0).as("connreq"),
                        ifnull(sum(TABLE.CALLBACK), 0).as("callback"),
                        ifnull(sum(TABLE.CALLBACK_SUCCESS), 0).as("callbackSuccess"),
                        ifnull(sum(TABLE.CANCEL_CALLBACK), 0).as("cancelCallback"))
                .from(TABLE)
                .where(compareCompanyId())
//                .and(TABLE.DCONTEXT.notEqual("pers_context"))
                .and(TABLE.STAT_DATE.eq(date(now())))
                .fetchInto(CenterStatResponse.class).stream().findFirst().orElse(new CenterStatResponse());
    }

    public Map<Byte, DashServiceStatResponse> getHourToInbound(String svcNumber, String huntNumber) {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        return dsl.select(TABLE.STAT_HOUR)
                .select(sum(TABLE.SUCCESS).as("successCnt"))
                .from(TABLE)
                .where(svcNumber.equals("") ? noCondition() : TABLE.SERVICE_NUMBER.eq(svcNumber))
                .and(huntNumber.equals("") ? noCondition() : TABLE.HUNT_NUMBER.eq(huntNumber))
                .and(TABLE.STAT_DATE.eq(date(now())))
                .and(TABLE.STAT_HOUR.eq((byte) currentHour)
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 1))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 2))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 3))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 4))))
                        .or(TABLE.STAT_HOUR.eq((byte) ((currentHour - 5))))
                )
                .and(TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
//                .and(TABLE.DCONTEXT.eq("hunt_context")/*.or(TABLE.DCONTEXT.eq("inbound"))*/)
                .groupBy(TABLE.STAT_HOUR)
                .fetchMap(TABLE.STAT_HOUR, DashServiceStatResponse.class);
    }

    public DashInboundChartResponse getInboundChartResponseMap() {
        final Map<Byte, DashInboundChartDataResponse> data =
                dsl.select(TABLE.STAT_HOUR,
                                ifnull(sum(TABLE.TOTAL), 0).as("totalCnt"),
                                ifnull(sum(TABLE.ONLYREAD), 0).as("onlyReadCnt"),
                                ifnull(sum(TABLE.CONNREQ), 0).as("connReqCnt"),
                                ifnull(sum(TABLE.SUCCESS), 0).as("successCnt"),
                                ifnull(sum(TABLE.CANCEL), 0).as("cancelCnt"),
                                ifnull(sum(TABLE.CALLBACK_SUCCESS), 0).as("callbackCnt"))
                        .from(TABLE)
                        .where(TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
                        .and(TABLE.STAT_DATE.eq(date(now())))
                        .and(TABLE.DCONTEXT.in("inbound", "hunt_context", "busy_context"))
                        .groupBy(TABLE.STAT_HOUR)
                        .fetchMap(TABLE.STAT_HOUR, DashInboundChartDataResponse.class);


        DashInboundChartResponse res = new DashInboundChartResponse();

        for (int i = 0; i < 24; i++) {
            byte hour = (byte) i;
            res.getInboundChat().put(hour, data.get(hour));
        }

        return res;
    }

    public Map<Byte, Integer> getMaximumNumberOfWaitCountByTime() {
        return dsl.select(TABLE.STAT_HOUR, ifnull(sum(TABLE.CONNREQ), 0).as(TABLE.CONNREQ))
                .from(TABLE)
                .where(TABLE.STAT_DATE.eq(DSL.currentDate()))
                .and(TABLE.DCONTEXT.notEqual("pers_context"))
                .groupBy(TABLE.STAT_HOUR)
                .fetchMap(TABLE.STAT_HOUR, TABLE.CONNREQ);
    }

    public Map<?, BigDecimal> getSuccessPer() {

        Table<?> A = table(select(
                TABLE.HUNT_NUMBER.as("hunt_number")
                , ifnull(sum(TABLE.SUCCESS).divide(sum(TABLE.TOTAL)).mul(100), 0).as("per")
        )
                .from(TABLE)
                .where(TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(TABLE.HUNT_NUMBER.isNotNull().and(TABLE.HUNT_NUMBER.notEqual("")))
                .and(TABLE.STAT_DATE.eq(date(now())))
                .groupBy(TABLE.HUNT_NUMBER)).as("A");

        return dsl.select(
                        QUEUE_NAME.NUMBER
                        , ifnull(A.field("per"), 0).as("per")
                )
                .from(QUEUE_NAME)
                .leftJoin(A)
                .on(QUEUE_NAME.NUMBER.eq((Field<String>) A.field("hunt_number")))
                .where(QUEUE_NAME.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .fetchMap(QUEUE_NAME.NUMBER, (Field<BigDecimal>) A.field("per"));
    }


}
