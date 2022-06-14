package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.HistoryUploadInfo;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.search.MaindbUploadSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.HISTORY_UPLOAD_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.MAINDB_GROUP;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class MaindbUploadRepository extends EicnBaseRepository<HistoryUploadInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryUploadInfo, String> {
    protected final Logger logger = LoggerFactory.getLogger(MaindbGroupRepository.class);

    public MaindbUploadRepository() {
        super(HISTORY_UPLOAD_INFO, HISTORY_UPLOAD_INFO.UPLOAD_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryUploadInfo.class);
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .innerJoin(MAINDB_GROUP)
		        .on(HISTORY_UPLOAD_INFO.GROUP_ID.eq(MAINDB_GROUP.SEQ)
                    .and(HISTORY_UPLOAD_INFO.KIND.eq(CommonTypeKind.MAIN_DB.getCode())))
                .where();
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryUploadInfo> pagination(MaindbUploadSearchRequest search) {
        return super.pagination(search, conditions(search), Collections.singletonList(HISTORY_UPLOAD_INFO.UPLOAD_DATE.desc()));
    }

    private List<Condition> conditions(MaindbUploadSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    return conditions;
                case MINE:
                case GROUP:
                    conditions.add(MAINDB_GROUP.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        if (search.getStartDate() != null)
            conditions.add(HISTORY_UPLOAD_INFO.UPLOAD_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if (search.getEndDate() != null)
            conditions.add(HISTORY_UPLOAD_INFO.UPLOAD_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (isNotEmpty(search.getMaindbGroupId()))
            conditions.add(HISTORY_UPLOAD_INFO.GROUP_ID.eq(Integer.valueOf(search.getMaindbGroupId())));

        return conditions;
    }
}
