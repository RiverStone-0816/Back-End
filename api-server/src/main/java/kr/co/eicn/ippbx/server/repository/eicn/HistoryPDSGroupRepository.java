package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.HistoryPdsGroup;
import kr.co.eicn.ippbx.model.dto.eicn.HistoryPdsGroupResponse;
import kr.co.eicn.ippbx.model.dto.eicn.HistoryPdsResearchGroupResponse;
import kr.co.eicn.ippbx.model.enums.PDSGroupConnectKind;
import kr.co.eicn.ippbx.model.enums.PDSGroupExecuteStatus;
import kr.co.eicn.ippbx.model.enums.PDSGroupResultKind;
import kr.co.eicn.ippbx.model.search.PDSHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.HistoryPdsGroupSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.HISTORY_PDS_GROUP;

@Getter
@Repository
public class HistoryPDSGroupRepository extends EicnBaseRepository<HistoryPdsGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup, Integer> {
    private final Logger logger = LoggerFactory.getLogger(HistoryPDSGroupRepository.class);

    public HistoryPDSGroupRepository() {
        super(HISTORY_PDS_GROUP, HISTORY_PDS_GROUP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup.class);

        addOrderingField(HISTORY_PDS_GROUP.START_DATE.desc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup> pagination(PDSHistorySearchRequest search) {
        return pagination(search, conditions(search));
    }

    private List<Condition> conditions(PDSHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(HISTORY_PDS_GROUP.START_DATE.ge(DSL.timestamp(search.getStartDate())));
        if (search.getEndDate() != null)
            conditions.add(HISTORY_PDS_GROUP.START_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (search.getSeq() != null)
            conditions.add(HISTORY_PDS_GROUP.PDS_GROUP_ID.eq(search.getSeq()));

        if (search.getExecuteName() != null)
            conditions.add(HISTORY_PDS_GROUP.EXECUTE_NAME.like("%" + search.getExecuteName() + "%"));

        conditions.add(HISTORY_PDS_GROUP.PDS_STATUS.notEqual(""));
        conditions.add(HISTORY_PDS_GROUP.PDS_STATUS.notEqual("I"));

        return conditions;
    }

    public void deleteByExecuteId(String executeId) {
        delete(HISTORY_PDS_GROUP.EXECUTE_ID.eq(executeId));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup> findAllGroupId(Integer groupId) {
        return findAll(HISTORY_PDS_GROUP.PDS_GROUP_ID.eq(groupId));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup> findAllByExecuteId(String executeId) {
        return findAll(HISTORY_PDS_GROUP.EXECUTE_ID.eq(executeId));
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup> findAll(HistoryPdsGroupSearchRequest search) {
        orderByFields.add(HISTORY_PDS_GROUP.START_DATE.desc());
        return findAll(conditions(search));
    }

    private List<Condition> conditions(HistoryPdsGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(HISTORY_PDS_GROUP.START_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if (search.getEndDate() != null)
            conditions.add(HISTORY_PDS_GROUP.START_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (StringUtils.isNotEmpty(search.getExecuteId()))
            conditions.add(HISTORY_PDS_GROUP.EXECUTE_ID.eq(search.getExecuteId()));

        if (search.getConnectKinds() != null && !search.getConnectKinds().isEmpty())
            conditions.add(HISTORY_PDS_GROUP.CONNECT_KIND.in(search.getConnectKinds()));

        return conditions;
    }

    public List<HistoryPdsGroupResponse> getResultExecuteList() {
        return dsl.select(HISTORY_PDS_GROUP.PDS_GROUP_ID,
                          HISTORY_PDS_GROUP.EXECUTE_ID,
                          HISTORY_PDS_GROUP.EXECUTE_NAME,
                          HISTORY_PDS_GROUP.PDS_TYPE,
                          HISTORY_PDS_GROUP.RESULT_TYPE)
                .from(HISTORY_PDS_GROUP)
                .where(compareCompanyId())
                .and(HISTORY_PDS_GROUP.RESULT_KIND.eq(PDSGroupResultKind.RS.getCode()))
                .and(HISTORY_PDS_GROUP.PDS_STATUS.ne(PDSGroupExecuteStatus.EXCLUDED.getCode()))
                .orderBy(HISTORY_PDS_GROUP.START_DATE.desc())
                .fetchInto(HistoryPdsGroupResponse.class);
    }

    public List<HistoryPdsResearchGroupResponse> getResearchExecuteList() {
        return dsl.select(HISTORY_PDS_GROUP.PDS_GROUP_ID,
                          HISTORY_PDS_GROUP.EXECUTE_ID,
                          HISTORY_PDS_GROUP.EXECUTE_NAME,
                          HISTORY_PDS_GROUP.PDS_TYPE,
                          HISTORY_PDS_GROUP.CONNECT_DATA)
                .from(HISTORY_PDS_GROUP)
                .where(compareCompanyId())
                .and(HISTORY_PDS_GROUP.CONNECT_KIND.eq(PDSGroupConnectKind.ARS_RSCH.getCode()))
                .and(HISTORY_PDS_GROUP.PDS_STATUS.ne(PDSGroupExecuteStatus.EXCLUDED.getCode()))
                .orderBy(HISTORY_PDS_GROUP.START_DATE.desc())
                .fetchInto(HistoryPdsResearchGroupResponse.class);
    }
}
