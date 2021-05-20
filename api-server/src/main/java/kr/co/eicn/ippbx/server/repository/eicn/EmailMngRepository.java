package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailServiceInfo;
import kr.co.eicn.ippbx.model.search.EmailMngSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailServiceInfo.EMAIL_SERVICE_INFO;

@Getter
@Repository
public class EmailMngRepository extends EicnBaseRepository<EmailServiceInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo, Integer>{
    private final Logger logger = LoggerFactory.getLogger(EmailMngRepository.class);

    public EmailMngRepository() {
        super(EMAIL_SERVICE_INFO, EMAIL_SERVICE_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo.class);
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo> pagination(EmailMngSearchRequest search) {
        orderByFields.add(EMAIL_SERVICE_INFO.SEQ.asc());
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(EmailMngSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }
}
