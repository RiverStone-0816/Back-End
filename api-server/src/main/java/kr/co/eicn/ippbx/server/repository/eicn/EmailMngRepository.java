package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailServiceInfo;
import kr.co.eicn.ippbx.model.search.EmailMngSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailServiceInfo.EMAIL_SERVICE_INFO;

@Getter
@Repository
public class EmailMngRepository extends EicnBaseRepository<EmailServiceInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo, Integer> {
    private final Logger logger = LoggerFactory.getLogger(EmailMngRepository.class);

    public EmailMngRepository() {
        super(EMAIL_SERVICE_INFO, EMAIL_SERVICE_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo.class);
        orderByFields.add(EMAIL_SERVICE_INFO.SEQ.asc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo> pagination(EmailMngSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo> findAll(EmailMngSearchRequest search) {
        return super.findAll(conditions(search));
    }

    private List<Condition> conditions(EmailMngSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotBlank(search.getServiceName()))
            conditions.add(EMAIL_SERVICE_INFO.SERVICE_NAME.eq(search.getServiceName().trim()));

        if (StringUtils.isNotBlank(search.getMailUserName()))
            conditions.add(EMAIL_SERVICE_INFO.MAIL_USER_NAME.eq(search.getMailUserName().trim()));

        return conditions;
    }

    /**
     * 서비스명 중복 체크
     */
    public Boolean isDuplicateServiceName(String serviceName, Integer seq) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(EMAIL_SERVICE_INFO.SERVICE_NAME.eq(serviceName));

        if (ObjectUtils.isNotEmpty(seq))
            conditions.add(EMAIL_SERVICE_INFO.SEQ.ne(seq));

        return fetchCount(conditions) > 0;
    }


    /**
     * 수신 접속 계정 중복 체크
     */
    public Boolean isDuplicateMailUserName(String mailUserName, Integer seq) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(EMAIL_SERVICE_INFO.MAIL_USER_NAME.eq(mailUserName));

        if (ObjectUtils.isNotEmpty(seq))
            conditions.add(EMAIL_SERVICE_INFO.SEQ.ne(seq));

        return fetchCount(conditions) > 0;
    }
}
