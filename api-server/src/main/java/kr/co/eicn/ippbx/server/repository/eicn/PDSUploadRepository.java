package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.HistoryPdsUpload;
import kr.co.eicn.ippbx.server.model.search.PDSUploadSearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.HISTORY_PDS_UPLOAD;

@Getter
@Repository
public class PDSUploadRepository extends EicnBaseRepository<HistoryPdsUpload, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsUpload, String> {
    protected final Logger logger = LoggerFactory.getLogger(PDSUploadRepository.class);

    public PDSUploadRepository() {
        super(HISTORY_PDS_UPLOAD, HISTORY_PDS_UPLOAD.UPLOAD_ID, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsUpload.class);
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsUpload> pagination(PDSUploadSearchRequest search) {
        return super.pagination(search, conditions(search), Arrays.asList(HISTORY_PDS_UPLOAD.TRY_CNT.asc()));
    }

    private List<Condition> conditions(PDSUploadSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(HISTORY_PDS_UPLOAD.UPLOAD_DATE.ge(DSL.timestamp(search.getStartDate())));

        if (search.getEndDate() != null)
            conditions.add(HISTORY_PDS_UPLOAD.UPLOAD_DATE.le(DSL.timestamp(search.getEndDate())));

        if (search.getPdsGroup() != null)
            conditions.add(HISTORY_PDS_UPLOAD.PDS_GROUP_ID.eq(search.getPdsGroup()));

        return conditions;
    }

}
