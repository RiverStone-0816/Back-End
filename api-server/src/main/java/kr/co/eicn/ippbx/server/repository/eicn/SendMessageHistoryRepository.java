package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendMessage;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendMessage.SEND_MESSAGE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class SendMessageHistoryRepository extends EicnBaseRepository<SendMessage, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessage, Long> {
    protected final Logger logger = LoggerFactory.getLogger(SendMessageHistoryRepository.class);

    public SendMessageHistoryRepository() {
        super(SEND_MESSAGE, SEND_MESSAGE.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessage.class);

        orderByFields.add(SEND_MESSAGE.SEND_DATE.desc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendMessage> pagination(SendMessageSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(SendMessageSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getStartDate() != null)
            conditions.add(SEND_MESSAGE.SEND_DATE.ge(Timestamp.valueOf(search.getStartDate() + " 00:00:00")));

        if(search.getEndDate()!= null)
            conditions.add(SEND_MESSAGE.SEND_DATE.le(Timestamp.valueOf(search.getEndDate() + " 23:59:59")));

        if(isNotEmpty(search.getTarget()))
            conditions.add(SEND_MESSAGE.TARGET.like("%" + search.getTarget() + "%"));

        return conditions;
    }

}
