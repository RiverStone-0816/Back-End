package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatOutbound;
import kr.co.eicn.ippbx.model.dto.eicn.DashServiceStatResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatOutboundEntity;
import kr.co.eicn.ippbx.model.search.AbstractStatSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;

@Getter
public class StatOutboundRepository extends StatDBBaseRepository<CommonStatOutbound, StatOutboundEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatOutboundRepository.class);

    private final CommonStatOutbound TABLE;

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

    public List<StatOutboundEntity> findAll(AbstractStatSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(AbstractStatSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        standardTime = search.getTimeUnit();

        conditions.add(getOutboundCondition(search));

        return conditions;
    }

    public Map<Byte, DashServiceStatResponse> getHourOutInbound() {
        int currentHour = new java.util.Date().getHours();
        Map<Byte, DashServiceStatResponse> graphData = dsl.select(TABLE.STAT_HOUR)
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

        return graphData;
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
