package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailList;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.EmailConsultationHistoryFormRequest;
import kr.co.eicn.ippbx.model.search.EmailConsultationHistorySearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.*;
import static kr.co.eicn.ippbx.util.StringUtils.subStringBytes;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class EmailConsultationHistoryRepository extends EicnBaseRepository<EmailList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailList, UInteger> {
    protected final Logger logger = LoggerFactory.getLogger(EmailConsultationHistoryRepository.class);

    WebSecureHistoryRepository webSecureHistoryRepository;

    EmailConsultationHistoryRepository(WebSecureHistoryRepository webSecureHistoryRepository) {
        super(EMAIL_LIST, EMAIL_LIST.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailList.class);
        this.webSecureHistoryRepository = webSecureHistoryRepository;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailList> pagination(EmailConsultationHistorySearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public void redistribution(EmailConsultationHistoryFormRequest form) {
        for(int i = 0; i<form.getIds().size(); i++) {
            final Integer id = form.getIds().get(i);

            dsl.update(EMAIL_LIST)
                    .set(EMAIL_LIST.USERID, form.getUserIds().get(i % form.getUserIds().size()))
                    .where(EMAIL_LIST.COMPANY_ID.eq(getCompanyId()))
                    .and(EMAIL_LIST.ID.eq(UInteger.valueOf(id)))
                    .and(EMAIL_LIST.RESULT_CODE.eq(""))
                    .and(EMAIL_LIST.USERID_TR.eq(""))
                    .execute();
        }

        webSecureHistoryRepository.insert(WebSecureActionType.EMAIL, WebSecureActionSubType.REDIST, subStringBytes(String.join("|", form.getUserIds()), 400));
    }

    private List<Condition> conditions(EmailConsultationHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getStartDate() != null)
            conditions.add(EMAIL_LIST.SENT_DATE.ge(DSL.timestamp(search.getStartDate())));

        if(search.getEndDate() != null)
            conditions.add(EMAIL_LIST.SENT_DATE.le(DSL.timestamp(search.getEndDate())));

        if(isNotEmpty(search.getUserId()))
            conditions.add(EMAIL_LIST.USERID.eq(search.getUserId()));

        if(search.getResultCode() != null)
            conditions.add(EMAIL_LIST.RESULT_CODE.eq(search.getResultCode().getCode()));

        if(isNotEmpty(search.getFromEmail()))
            conditions.add(EMAIL_LIST.FROM_EMAIL.like("%" + search.getFromEmail() + "%"));

        if(isNotEmpty(search.getFromName()))
            conditions.add(EMAIL_LIST.FROM_NAME.like("%" + search.getFromName() + "%"));

        if(isNotEmpty(search.getCustomName()))
            conditions.add(EMAIL_LIST.CUSTOM_NAME.like("%" + search.getCustomName() + "%"));

        if(isNotEmpty(search.getCustomCompanyName()))
            conditions.add(EMAIL_LIST.CUSTOM_COMPANY_NAME.like("%" + search.getCustomCompanyName() +"%"));

        return conditions;
    }
}
