package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatTalk;
import kr.co.eicn.ippbx.model.entity.statdb.StatTalkEntity;
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
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.field;

@Getter
public class TalkStatisticsRepository extends StatDBBaseRepository<CommonStatTalk, StatTalkEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(TalkStatisticsRepository.class);

    private final CommonStatTalk TABLE;

    private String type = "";

    public TalkStatisticsRepository(String companyId) {
        super(new CommonStatTalk(companyId), new CommonStatTalk(companyId).SEQ, StatTalkEntity.class);
        TABLE = new CommonStatTalk(companyId);
        addField(ifnull(sum(TABLE.START_ROOM_CNT), 0).as(TABLE.START_ROOM_CNT),
                ifnull(sum(TABLE.END_ROOM_CNT), 0).as(TABLE.END_ROOM_CNT),
                ifnull(sum(TABLE.IN_MSG_CNT), 0).as(TABLE.IN_MSG_CNT),
                ifnull(sum(TABLE.OUT_MSG_CNT), 0).as(TABLE.OUT_MSG_CNT),
                ifnull(sum(TABLE.AUTO_MENT_CNT), 0).as(TABLE.AUTO_MENT_CNT),
                ifnull(sum(TABLE.AUTO_MENT_EXCEED_CNT), 0).as(TABLE.AUTO_MENT_EXCEED_CNT));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        if (type.equals("day"))
            query.groupBy(TABLE.STAT_DATE);
        else if (type.equals("hour"))
            query.groupBy(TABLE.STAT_HOUR);
        else if (type.equals("person"))
            query.groupBy(TABLE.USERID);

        return query.where();
    }

    public List<StatTalkEntity> dailyStatList(TalkStatisticsSearchRequest search) {
        addField(TABLE.STAT_DATE);
        orderByFields.add(TABLE.STAT_DATE.asc());
        type = "day";
        return super.findAll(condition(search));
    }

    public List<StatTalkEntity> hourlyStatList(TalkStatisticsHourlySearchRequest search) {
        addField(TABLE.STAT_HOUR);
        type = "hour";

        List<Condition> conditions = condition(search);
        if (search.getStartHour() != null)
            conditions.add(TABLE.STAT_HOUR.ge(search.getStartHour()));
        if (search.getEndHour() != null)
            conditions.add(TABLE.STAT_HOUR.le(search.getEndHour()));
        orderByFields.add(TABLE.STAT_HOUR.asc());

        return super.findAll(conditions);
    }

    public List<StatTalkEntity> personStatList(TalkStatisticsSearchRequest search) {
        addField(TABLE.USERID);
        type = "person";
        List<Condition> conditions = condition(search);
        conditions.add(TABLE.USERID.notEqual(""));

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
                , ifnull(sum(TABLE.END_ROOM_CNT), 0).as("talk_success"))
                .from(TABLE)
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(currentDate()))
                .and(TABLE.USERID.in(person))
                .groupBy(TABLE.USERID)
                .fetchMap(TABLE.USERID,field("talk_success"));
    }
}
