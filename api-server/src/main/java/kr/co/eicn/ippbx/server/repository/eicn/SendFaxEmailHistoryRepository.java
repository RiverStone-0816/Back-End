package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendFaxEmail;
import kr.co.eicn.ippbx.model.search.SendFaxEmailHistorySearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendFaxEmail.SEND_FAX_EMAIL;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class SendFaxEmailHistoryRepository extends EicnBaseRepository<SendFaxEmail, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFaxEmail, Long> {
    protected final Logger logger = LoggerFactory.getLogger(SendFaxEmailHistoryRepository.class);

    public SendFaxEmailHistoryRepository() {
        super(SEND_FAX_EMAIL, SEND_FAX_EMAIL.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFaxEmail.class);

        orderByFields.add(SEND_FAX_EMAIL.ID.asc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFaxEmail> pagination(SendFaxEmailHistorySearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(SendFaxEmailHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getStartDate() != null)
            conditions.add(SEND_FAX_EMAIL.SEND_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if(search.getEndDate() != null)
            conditions.add(SEND_FAX_EMAIL.SEND_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if(isNotEmpty(search.getTarget()))
            conditions.add(SEND_FAX_EMAIL.TARGET.like("%" + search.getTarget() + "%"));

        if(isNotEmpty(search.getType()))
            conditions.add(SEND_FAX_EMAIL.TYPE.like(search.getType()));

        return conditions;
    }
}
