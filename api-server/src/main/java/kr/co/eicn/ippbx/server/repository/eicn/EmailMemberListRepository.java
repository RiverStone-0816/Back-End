package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EmailMemberList;
import kr.co.eicn.ippbx.model.entity.eicn.EmailMemberListEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.EMAIL_MEMBER_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;

@Getter
@Repository
public class EmailMemberListRepository extends EicnBaseRepository<EmailMemberList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberList, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(EmailMemberListRepository.class);

    public EmailMemberListRepository() {
        super(EMAIL_MEMBER_LIST, EMAIL_MEMBER_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberList.class);
    }

    public List<EmailMemberListEntity> getEmailMemberListEntities() {
        return dsl.select(EMAIL_MEMBER_LIST.fields())
                .select(PERSON_LIST.fields())
                .from(EMAIL_MEMBER_LIST)
                .join(PERSON_LIST)
                .on(EMAIL_MEMBER_LIST.USERID.eq(PERSON_LIST.ID))
                .where(compareCompanyId())
                .fetch(record -> {
                    final EmailMemberListEntity entity = record.into(EMAIL_MEMBER_LIST).into(EmailMemberListEntity.class);
                    entity.setPerson(record.into(PERSON_LIST).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class));
                    return entity;
                });
    }
}
