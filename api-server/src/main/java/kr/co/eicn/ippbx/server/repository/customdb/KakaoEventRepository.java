package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonKakaoEvent;
import kr.co.eicn.ippbx.model.form.ChatbotSendEventFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotEventHistorySearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class KakaoEventRepository extends CustomDBBaseRepository<CommonKakaoEvent, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoEvent, Integer>{
    private final Logger logger = LoggerFactory.getLogger(KakaoEventRepository.class);

    private final CommonKakaoEvent TABLE;

    public KakaoEventRepository(String companyId) {
        super(new CommonKakaoEvent(companyId), new CommonKakaoEvent(companyId).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoEvent.class);
        TABLE = new CommonKakaoEvent(companyId);

        orderByFields.add(TABLE.INSERT_DATE.desc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoEvent> getPagination(ChatbotEventHistorySearchRequest search) {
        return super.pagination(search, getConditions(search));
    }

    public List<Condition> getConditions(ChatbotEventHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()) && Objects.nonNull(search.getStartHour()))
            conditions.add(TABLE.INSERT_DATE.ge(Timestamp.valueOf(search.getStartDate() + " " + search.getStartHour() + ":00:00")));

        if (Objects.nonNull(search.getEndDate()) && Objects.nonNull(search.getEndHour()))
            conditions.add(TABLE.INSERT_DATE.le(Timestamp.valueOf(search.getEndDate() + " " + search.getEndHour() + ":00:00")));

        if (StringUtils.isNotEmpty(search.getBotId()))
            conditions.add(TABLE.BOT_ID.eq(search.getBotId()));

        if (StringUtils.isNotEmpty(search.getEventName()))
            conditions.add(TABLE.EVENT_NAME.eq(search.getEventName()));

        if (StringUtils.isNotEmpty(search.getProfileName()))
            conditions.add(TABLE.USER_NAME.like("%" + search.getProfileName() + "%"));

        return conditions;
    }

    public Integer insert(ChatbotSendEventFormRequest form) {
        return dsl.insertInto(TABLE)
                .set(TABLE.BOT_ID, form.getBotId())
                .set(TABLE.BOT_NAME, form.getBotName())
                .set(TABLE.EVENT_NAME, form.getEventName())
                .set(TABLE.USER_TYPE, form.getUserType())
                .set(TABLE.USER_ID, form.getUserId())
                .set(TABLE.USER_NAME, form.getUserName())
                .set(TABLE.USER_DATA, form.getEventData())
                .set(TABLE.RESPONSE_BLOCK_NAME, "")
                .set(TABLE.RESPONSE_BLOCK_ID, "")
                .set(TABLE.TASK_ID, "")
                .returningResult(TABLE.SEQ)
                .fetchOne().value1();
    }
}
