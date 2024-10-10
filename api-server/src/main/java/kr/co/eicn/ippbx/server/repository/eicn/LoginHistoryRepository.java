package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.LoginHistory;
import kr.co.eicn.ippbx.model.search.LoginHistorySearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.LOGIN_HISTORY;

@Getter
@Repository
public class LoginHistoryRepository extends EicnBaseRepository<LoginHistory, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.LoginHistory, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(LoginHistory.class);

    public LoginHistoryRepository() {
        super(LOGIN_HISTORY, LOGIN_HISTORY.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.LoginHistory.class);

        orderByFields.add(LOGIN_HISTORY.LOGIN_DATE.desc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.LoginHistory> pagination(LoginHistorySearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(LoginHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null) {
            conditions.add(LOGIN_HISTORY.LOGIN_DATE.ge(DSL.timestamp(search.getStartDate())));
        }
        if (search.getEndDate() != null) {
            conditions.add(LOGIN_HISTORY.LOGIN_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));
        }
        if (StringUtils.isNotEmpty(search.getUserId())) {
            conditions.add(LOGIN_HISTORY.USERID.like("%" + search.getUserId() + "%"));
        }
        if (StringUtils.isNotEmpty(search.getUserName())) {
            conditions.add(LOGIN_HISTORY.USER_NAME.like("%" + search.getUserName() + "%"));
        }
        if (StringUtils.isNotEmpty(search.getExtension())) {
            conditions.add(LOGIN_HISTORY.EXTENSION.like("%" + search.getExtension() + "%"));
        }

        return conditions;
    }
}
