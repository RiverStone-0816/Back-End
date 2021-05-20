package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendFile;
import kr.co.eicn.ippbx.model.search.SendFileSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendFile.SEND_FILE;

@Getter
@Repository
public class SendFileRepository extends EicnBaseRepository<SendFile, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFile, Long> {
    protected final Logger logger = LoggerFactory.getLogger(SendFileRepository.class);

   SendFileRepository() {
        super(SEND_FILE, SEND_FILE.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFile.class);
   }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFile> pagination(SendFileSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public Long nextSequence() {
        final SendFile sequenceSeed = SEND_FILE.as("SEQUENCE_SEED");
        return dsl.select(DSL.ifnull(DSL.max(sequenceSeed.ID), 0).add(1)).from(sequenceSeed).fetchOneInto(Long.class);
    }

    public void deleteByFile(DSLContext dslContext, final String file) {
        dslContext.deleteFrom(SEND_FILE)
                .where(SEND_FILE.FILE_NAME.eq(file))
                .and(compareCompanyId())
                .execute();
    }

    private List<Condition> conditions(SendFileSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;

    }
}
