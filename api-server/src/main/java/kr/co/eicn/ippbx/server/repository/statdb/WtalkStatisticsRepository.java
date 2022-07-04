package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatWtalk;
import kr.co.eicn.ippbx.model.entity.statdb.StatWtalkEntity;
import kr.co.eicn.ippbx.model.search.TalkStatisticsHourlySearchRequest;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;

@Getter
public class WtalkStatisticsRepository extends StatDBBaseRepository<CommonStatWtalk, StatWtalkEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WtalkStatisticsRepository.class);

    private final CommonStatWtalk TABLE;

    private String type = "";

    public WtalkStatisticsRepository(String companyId) {
        super(new CommonStatWtalk(companyId), new CommonStatWtalk(companyId).SEQ, StatWtalkEntity.class);
        TABLE = new CommonStatWtalk(companyId);

        addField(TABLE.ACTION_TYPE);
        addField(ifnull(sum(TABLE.CNT), 0).as(TABLE.CNT));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        switch (type) {
            case "day":
                query.groupBy(TABLE.STAT_DATE);
                break;
            case "hour":
                query.groupBy(TABLE.STAT_HOUR);
                break;
            case "person":
                query.groupBy(TABLE.USERID);
                break;
        }

        query.groupBy(TABLE.ACTION_TYPE);


        return query.where();
    }

    public List<StatWtalkEntity> dailyStatList(TalkStatisticsSearchRequest search) {
        addField(TABLE.STAT_DATE);
        type = "day";
        return super.findAll(dsl, condition(search), Collections.singletonList(TABLE.STAT_DATE.asc()));
    }

    public List<StatWtalkEntity> hourlyStatList(TalkStatisticsHourlySearchRequest search) {
        addField(TABLE.STAT_HOUR);
        type = "hour";

        List<Condition> conditions = condition(search);
        if (search.getStartHour() != null)
            conditions.add(TABLE.STAT_HOUR.ge(search.getStartHour()));
        if (search.getEndHour() != null)
            conditions.add(TABLE.STAT_HOUR.le(search.getEndHour()));

        return super.findAll(dsl, conditions, Collections.singletonList(TABLE.STAT_HOUR.asc()));
    }

    public List<StatWtalkEntity> personStatList(TalkStatisticsSearchRequest search) {
        addField(TABLE.USERID);
        type = "person";
        List<Condition> conditions = condition(search);
//        conditions.add(TABLE.USERID.notEqual(""));

        return super.findAll(conditions);
    }

    public List<Condition> condition(TalkStatisticsSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(TABLE.STAT_DATE.ge(search.getStartDate()));
        if (search.getEndDate() != null)
            conditions.add(TABLE.STAT_DATE.le(search.getEndDate()));
        if (StringUtils.isNotEmpty(search.getSenderKey()))
            conditions.add(TABLE.SENDER_KEY.eq(search.getSenderKey()));

        return conditions;
    }

    public Map<String, Object> findChatUserTalkMonitor(List<String> person){
        return dsl.select(TABLE.USERID
                , ifnull(sum(TABLE.CNT), 0).as("talk_success"))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.ACTION_TYPE.eq("USER_END_ROOM").or(TABLE.ACTION_TYPE.eq("CUSTOM_END_ROOM")))
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.USERID.in(person))
                .groupBy(TABLE.USERID)
                .fetchMap(TABLE.USERID,field("talk_success"));
    }
}
