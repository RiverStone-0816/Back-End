package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatQueueWait;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.StatQueueWait;
import kr.co.eicn.ippbx.model.dto.eicn.DashResultChartResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Map;

import static org.jooq.impl.DSL.*;

@Getter
public class StatQueueWaitRepository extends StatDBBaseRepository<CommonStatQueueWait, StatQueueWait, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatQueueWaitRepository.class);

    private final CommonStatQueueWait TABLE;

    public StatQueueWaitRepository(String companyId) {
        super(new CommonStatQueueWait(companyId), new CommonStatQueueWait(companyId).SEQ, StatQueueWait.class);
        this.TABLE = new CommonStatQueueWait(companyId);
    }

    public Map<Byte, DashResultChartResponse> getHourToMaxWaitCnt() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        return dsl.select(TABLE.STAT_HOUR)
                .select(max(TABLE.MAX_WAIT).as("maxWaitCnt"))
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
//                .and(TABLE.DCONTEXT.eq("hunt_context")/*.or(TABLE.DCONTEXT.eq("inbound"))*/)
                .groupBy(TABLE.STAT_HOUR)
                .fetchMap(TABLE.STAT_HOUR, DashResultChartResponse.class);
    }

}
