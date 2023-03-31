package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMessageData;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
public class MessageDataRepository extends CustomDBBaseRepository<CommonMessageData, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMessageData, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(MessageDataRepository.class);
    private final CommonMessageData TABLE;

    public MessageDataRepository(String companyId) {
        super(new CommonMessageData(companyId), new CommonMessageData(companyId).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMessageData.class);
        TABLE = new CommonMessageData(companyId);

        orderByFields.add(TABLE.SEQ.desc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMessageData> pagination(SendMessageSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(SendMessageSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getStartDate() != null)
            conditions.add(TABLE.SEND_TIME.ge(Timestamp.valueOf(search.getStartDate() + " 00:00:00")));

        if(search.getEndDate()!= null)
            conditions.add(TABLE.SEND_TIME.le(Timestamp.valueOf(search.getEndDate() + " 23:59:59")));

        if(isNotEmpty(search.getTarget()))
            conditions.add(TABLE.PHONE_NUMBER.like("%" + search.getTarget() + "%"));

        return conditions;
    }
}
