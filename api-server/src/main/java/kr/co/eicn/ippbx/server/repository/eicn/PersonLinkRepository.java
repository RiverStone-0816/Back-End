package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonLink;
import kr.co.eicn.ippbx.server.model.form.PersonLinkFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonLink.PERSON_LINK;

@Getter
@Repository
public class PersonLinkRepository extends EicnBaseRepository<PersonLink, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonLink, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PersonLinkRepository.class);

    public PersonLinkRepository() {
        super(PERSON_LINK, PERSON_LINK.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonLink.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonLink> findAllByPersonId(String personId) {
        return findAll(PERSON_LINK.PERSON_ID.eq(personId));
    }

    public Integer insert(PersonLinkFormRequest form) {
        return dsl.insertInto(PERSON_LINK)
                .set(PERSON_LINK.NAME, form.getName())
                .set(PERSON_LINK.REFERENCE, form.getReference())
                .set(PERSON_LINK.PERSON_ID, g.getUser().getId())
                .set(PERSON_LINK.COMPANY_ID, getCompanyId())
                .returning(PERSON_LINK.SEQ)
                .fetchOne().value1();
    }

    public void update(Integer seq, PersonLinkFormRequest form) {
        // FIXME: 권한 제어
        dsl.update(PERSON_LINK)
                .set(PERSON_LINK.NAME, form.getName())
                .set(PERSON_LINK.REFERENCE, form.getReference())
                .where(PERSON_LINK.SEQ.eq(seq))
                .execute();
    }
}
