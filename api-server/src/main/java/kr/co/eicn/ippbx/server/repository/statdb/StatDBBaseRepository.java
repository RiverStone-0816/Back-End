package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.model.enums.ContextType;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.AbstractStatSearchRequest;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.repository.BaseRepository;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.TableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Date;
import java.util.Objects;

import static org.jooq.impl.DSL.*;

public abstract class StatDBBaseRepository<TABLE extends TableImpl<?>, ENTITY, PK> extends BaseRepository<TABLE, ENTITY, PK> {
    protected TableField<? extends Record, PK> primaryField;

    @Autowired
    @Qualifier(Constants.BEAN_DSL_STATDB)
    protected DSLContext dsl;
    protected SearchCycle standardTime;

    public StatDBBaseRepository(TABLE table, TableField<? extends Record, PK> primaryField, Class<ENTITY> entityClass) {
        super(table, entityClass);
        this.primaryField = primaryField;
    }

    @Override
    protected DSLContext dsl() {
        return dsl;
    }

    /**
     * 반드시 Override 해야 한다.
     */
    @Override
    protected Condition getCondition(PK key) {
        return compareCompanyId()
                .and(primaryField.eq(key));
    }

    protected Condition getInboundCondition(AbstractStatSearchRequest search) {
        final Field<String> field = table.field("dcontext", String.class);
        final Condition inboundContextCondition = field.in(ContextType.INBOUND.getCode(), ContextType.HUNT_CALL.getCode(), ContextType.CALL_BACK.getCode());

        return getDefaultCondition("inbound", inboundContextCondition, search);
    }

    protected Condition getOutboundCondition(AbstractStatSearchRequest search) {
        final Field<String> field = table.field("dcontext", String.class);
        final Condition outboundContextCondition = field.in(ContextType.OUTBOUND.getCode(), ContextType.HUNT_CALL.getCode());

        return getDefaultCondition("outbound", outboundContextCondition, search);
    }

    protected Condition getDefaultCondition(String type, Condition condition, AbstractStatSearchRequest search) {
        final Field<String> dcontext = table.field("dcontext", String.class);
        final Field<String> workTime = table.field("worktime_yn", String.class);
        final Field<Date> statDate = table.field("stat_date", Date.class);

        if (search.getPerson())
            condition = condition.or(dcontext.eq(ContextType.DIRECT_CALL.getCode()));
        if (search.getInner())
            condition = condition.or(dcontext.eq(type.equals("inbound") ? ContextType.INBOUND_INNER.getCode() : ContextType.OUTBOUND_INNER.getCode()));
        if (search.getWorkHour())
            condition = condition.and(workTime.eq("Y"));
        if (Objects.nonNull(search.getStartDate()))
            condition = condition.and(statDate.ge(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            condition = condition.and(statDate.le(search.getEndDate()));

        return condition;
    }

    protected void setTimeUnit(SelectJoinStep<Record> query) {
        final Field<Date> statDate = table.field("stat_date", Date.class);
        final Field<Integer> statHour = table.field("stat_hour", Integer.class);

        if (SearchCycle.DATE.equals(standardTime)) {
            query.groupBy(statDate);
        } else if (SearchCycle.HOUR.equals(standardTime)) {
            query.groupBy(statHour);
        } else if (SearchCycle.WEEK.equals(standardTime)) {
            query.groupBy(year(statDate));
            query.groupBy(week(statDate));
        } else if (SearchCycle.MONTH.equals(standardTime)) {
            query.groupBy(year(statDate));
            query.groupBy(month(statDate));
        } else if (SearchCycle.DAY_OF_WEEK.equals(standardTime)) {
            query.groupBy(dayOfWeek(statDate));
        }
    }
}
