package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.HistoryPdsGroup;
import kr.co.eicn.ippbx.server.model.search.PDSHistorySearchRequest;
import kr.co.eicn.ippbx.server.model.search.HistoryPdsGroupSearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.HISTORY_PDS_GROUP;

@Getter
@Repository
public class HistoryPDSGroupRepository extends EicnBaseRepository<HistoryPdsGroup, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup, Integer> {
    private final Logger logger = LoggerFactory.getLogger(HistoryPDSGroupRepository.class);

    public HistoryPDSGroupRepository() {
        super(HISTORY_PDS_GROUP, HISTORY_PDS_GROUP.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup.class);
        
        addOrderingField(HISTORY_PDS_GROUP.START_DATE.desc());
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup> pagination(PDSHistorySearchRequest search) {
        return pagination(search, conditions(search), Collections.singletonList(HISTORY_PDS_GROUP.STOP_DATE.desc()));
    }

    private List<Condition> conditions(PDSHistorySearchRequest search) {
        List<Condition> conditions = new ArrayList<>();
        if (search.getStartDate() != null)
            conditions.add(HISTORY_PDS_GROUP.START_DATE.ge(DSL.timestamp(search.getStartDate())));
        if (search.getEndDate() != null)
            conditions.add(HISTORY_PDS_GROUP.START_DATE.le(DSL.timestamp(search.getEndDate()+" 23:59:59")));
        if (search.getSeq() != null)
            conditions.add(HISTORY_PDS_GROUP.PDS_GROUP_ID.eq(search.getSeq()));
        conditions.add(HISTORY_PDS_GROUP.PDS_STATUS.notEqual(""));
        conditions.add(HISTORY_PDS_GROUP.PDS_STATUS.notEqual("I"));

        return conditions;
    }

    public void deleteByExecuteId(String executeId) {
        delete(HISTORY_PDS_GROUP.EXECUTE_ID.eq(executeId));
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup> findAllGroupId(Integer groupId) {
        return findAll(HISTORY_PDS_GROUP.PDS_GROUP_ID.eq(groupId));
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup> findAllByExecuteId(String executeId) {
        return findAll(HISTORY_PDS_GROUP.EXECUTE_ID.eq(executeId));
    }
    
    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup> findAll(HistoryPdsGroupSearchRequest search) {
        orderByFields.add(HISTORY_PDS_GROUP.START_DATE.desc());
        return findAll(conditions(search));
    }
    
    private List<Condition> conditions(HistoryPdsGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        
        if (search.getStartDate() != null)
            conditions.add(DSL.date(HISTORY_PDS_GROUP.START_DATE).ge(search.getStartDate()));
        
        if (search.getEndDate() != null)
            conditions.add(DSL.date(HISTORY_PDS_GROUP.START_DATE).le(search.getEndDate()));
        
        if (StringUtils.isNotEmpty(search.getExecuteId()))
            conditions.add(HISTORY_PDS_GROUP.EXECUTE_ID.eq(search.getExecuteId()));
        
        if (search.getConnectKinds() != null && !search.getConnectKinds().isEmpty() )
            conditions.add(HISTORY_PDS_GROUP.CONNECT_KIND.in(search.getConnectKinds()));
        
        return conditions;
    }
}
