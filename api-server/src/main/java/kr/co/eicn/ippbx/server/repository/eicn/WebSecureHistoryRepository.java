package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebSecureHistory;
import kr.co.eicn.ippbx.model.dto.eicn.LoginInfoResponse;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.search.WebSecureHistorySearchRequest;
import kr.co.eicn.ippbx.util.ContextUtil;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebSecureHistory.WEB_SECURE_HISTORY;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class WebSecureHistoryRepository extends EicnBaseRepository<WebSecureHistory, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebSecureHistory, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebSecureHistoryRepository.class);

    public WebSecureHistoryRepository() {
        super(WEB_SECURE_HISTORY, WEB_SECURE_HISTORY.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebSecureHistory.class);
        orderByFields.add(WEB_SECURE_HISTORY.INSERT_DATE.desc());
    }

    public void insert(WebSecureActionType actionId, WebSecureActionSubType actionSubId, String actionData) {
        insert(EMPTY, actionId, actionSubId, actionData);
    }

    public void updateWebLogDown(WebSecureActionType type) {
        insert(type, WebSecureActionSubType.DOWN, "");
    }

    public void insert(String extension, WebSecureActionType actionId, WebSecureActionSubType actionSubId, String actionData) {
        final HttpServletRequest request = ContextUtil.getRequest();
        final String ip = request.getHeader("HTTP_CLIENT_IP");
        dsl.insertInto(WEB_SECURE_HISTORY)
                .set(WEB_SECURE_HISTORY.INSERT_DATE, DSL.now())
                .set(WEB_SECURE_HISTORY.SECURE_SESSIONID, g.getUser().getId().toLowerCase().concat("_").concat(String.valueOf(Calendar.getInstance().getTimeInMillis())))
                .set(WEB_SECURE_HISTORY.SECURE_IP, StringUtils.isNotEmpty(ip) ? ip : request.getRemoteHost())
                .set(WEB_SECURE_HISTORY.USERID, g.getUser().getId())
                .set(WEB_SECURE_HISTORY.USER_NAME, g.getUser().getIdName())
                .set(WEB_SECURE_HISTORY.ID_TYPE, g.getUser().getIdType())
                .set(WEB_SECURE_HISTORY.EXTENSION, StringUtils.isNotEmpty(extension) ? extension : g.getUser().getExtension())
                .set(WEB_SECURE_HISTORY.ACTION_ID, actionId.getCode())
                .set(WEB_SECURE_HISTORY.ACTION_SUB_ID, actionSubId.getCode())
                .set(WEB_SECURE_HISTORY.ACTION_DATA, actionData)
                .set(WEB_SECURE_HISTORY.COMPANY_ID, getCompanyId())
                .execute();
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebSecureHistory> pagination(WebSecureHistorySearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(WebSecureHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()) && Objects.nonNull(search.getStartHour()))
            conditions.add(WEB_SECURE_HISTORY.INSERT_DATE.ge(DSL.timestamp(search.getStartDate() + " " + search.getStartHour() + ":00:00")));
        else if (search.getStartDate() != null)
            conditions.add(DSL.date(WEB_SECURE_HISTORY.INSERT_DATE).ge(search.getStartDate()));

        if (Objects.nonNull(search.getEndDate()) && Objects.nonNull(search.getEndHour()))
            conditions.add(WEB_SECURE_HISTORY.INSERT_DATE.le(DSL.timestamp(search.getEndDate() + " " + search.getEndHour() + ":59:59")));
        else if (search.getEndDate() != null)
            conditions.add(DSL.date(WEB_SECURE_HISTORY.INSERT_DATE).le(search.getEndDate()));

        if (isNotEmpty(search.getUserName()))
            conditions.add(WEB_SECURE_HISTORY.USER_NAME.like("%" + search.getUserName() + "%"));

        if (isNotEmpty(search.getActionId()))
            conditions.add(WEB_SECURE_HISTORY.ACTION_ID.eq(search.getActionId()));

        if (isNotEmpty(search.getActionSubId()))
            conditions.add(WEB_SECURE_HISTORY.ACTION_SUB_ID.eq(search.getActionSubId()));

        return conditions;
    }

    public LoginInfoResponse getLastLoginInfo(String userId) {
        return dsl.select(WEB_SECURE_HISTORY.fields())
                .from(WEB_SECURE_HISTORY)
                .where(compareCompanyId())
                .and(WEB_SECURE_HISTORY.ACTION_ID.eq(WebSecureActionType.LOGIN.getCode()))
                .and(WEB_SECURE_HISTORY.ACTION_SUB_ID.eq(WebSecureActionSubType.OK.getCode()))
                .and(WEB_SECURE_HISTORY.USERID.eq(userId))
                .orderBy(WEB_SECURE_HISTORY.INSERT_DATE.desc())
                .limit(2, 1)
                .fetchOneInto(LoginInfoResponse.class);
    }

    public void overwrite(Integer limit) {
        final List<Integer> webSecureHistories = dsl.select(WEB_SECURE_HISTORY.SEQ)
                .from(WEB_SECURE_HISTORY)
                .where(compareCompanyId())
                .orderBy(WEB_SECURE_HISTORY.INSERT_DATE.asc())
                .limit(limit)
                .fetchInto(Integer.class);

        for (Integer seq : webSecureHistories) {
            deleteOnIfNullThrow(seq);
        }
    }

    public void deleteIds(List<Integer> seq) {
        for (Integer el : seq) {
            if (el != null)
                deleteOnIfNullThrow(el);
        }
    }
}
