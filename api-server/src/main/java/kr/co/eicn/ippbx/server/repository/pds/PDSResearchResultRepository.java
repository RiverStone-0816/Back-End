package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSResearchResult;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.model.search.PDSResearchResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.server.service.PDSCustomInfoService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.pds.Tables.PDS_RESEARCH_RESULT;

@Getter
public class PDSResearchResultRepository extends PDSDbBaseRepository<CommonPDSResearchResult, PdsResearchResultEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PDSCustomInfoRepository.class);

    private final CommonPDSResearchResult TABLE;
    private final CommonPDSCustomInfo pdsCustomInfoTable;

    public PDSResearchResultRepository(String executeId, PDSCustomInfoService pdsCustomInfoService, HistoryPDSGroupRepository historyPDSGroupRepository) {
        super(new CommonPDSResearchResult(executeId), new CommonPDSResearchResult(executeId).SEQ, PdsResearchResultEntity.class);
        this.TABLE = new CommonPDSResearchResult(executeId);

        final List<HistoryPdsGroup> historyPdsGroups = historyPDSGroupRepository.findAllByExecuteId(executeId);
        if (historyPdsGroups.isEmpty())
            throw new IllegalStateException("unknown history_pds_group from execute_id: " + executeId);

        pdsCustomInfoTable = pdsCustomInfoService.getRepository(historyPdsGroups.get(0).getPdsGroupId()).getTABLE();

        addField(TABLE);
        addField(pdsCustomInfoTable);

        orderByFields.add(TABLE.RESULT_DATE.asc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query.join(pdsCustomInfoTable).on(pdsCustomInfoTable.PDS_SYS_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, PdsResearchResultEntity> getMapper() {
        return r -> {
            final PdsResearchResultEntity entity = r.into(TABLE).into(PdsResearchResultEntity.class);
            entity.setCustomInfo(r.into(pdsCustomInfoTable).into(PdsCustomInfo.class));
            return entity;
        };
    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE)
                .columns(PDS_RESEARCH_RESULT.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(PDS_RESEARCH_RESULT.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }

    public List<PdsResearchResultEntity> findAll(PDSResearchResultSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(PDSResearchResultSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(DSL.date(TABLE.RESULT_DATE).ge(search.getStartDate()));
        if (search.getEndDate() != null)
            conditions.add(DSL.date(TABLE.RESULT_DATE).le(search.getEndDate()));
        if (StringUtils.isNotEmpty(search.getExecuteId()))
            conditions.add(TABLE.EXECUTE_ID.eq(search.getExecuteId()));

        return conditions;
    }
}
