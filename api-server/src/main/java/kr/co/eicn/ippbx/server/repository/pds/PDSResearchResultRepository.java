package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSResearchResult;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.model.search.PDSResearchResultSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.pds.Tables.PDS_RESEARCH_RESULT;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@Getter
public class PDSResearchResultRepository extends PDSDbBaseRepository<CommonPDSResearchResult, PdsResearchResultEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PDSCustomInfoRepository.class);

    private final CommonPDSResearchResult TABLE;
    private final CommonPDSCustomInfo     PDS_CUSTOM_INFO_TABLE;

    public PDSResearchResultRepository(String name) {
        super(new CommonPDSResearchResult(substringAfter(name, "_")), new CommonPDSResearchResult(substringAfter(name, "_")).SEQ, PdsResearchResultEntity.class);
        this.TABLE = new CommonPDSResearchResult(substringAfter(name, "_"));
        PDS_CUSTOM_INFO_TABLE = new CommonPDSCustomInfo(substringBeforeLast(name, "_"));

        orderByFields.add(TABLE.RESULT_DATE.desc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(PDS_CUSTOM_INFO_TABLE).on(PDS_CUSTOM_INFO_TABLE.PDS_SYS_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, PdsResearchResultEntity> getMapper() {
        return r -> {
            final PdsResearchResultEntity entity = r.into(TABLE).into(PdsResearchResultEntity.class);
            entity.setCustomInfo(r.into(PDS_CUSTOM_INFO_TABLE).into(PdsCustomInfo.class));
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

    public Pagination<PdsResearchResultEntity> pagination(PDSResearchResultSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<PdsResearchResultEntity> findAll(PDSResearchResultSearchRequest search) {
        return super.findAll(conditions(search));
    }

    private List<Condition> conditions(PDSResearchResultSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (search.getEndDate() != null)
            conditions.add(TABLE.RESULT_DATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (StringUtils.isNotEmpty(search.getExecuteId()))
            conditions.add(TABLE.EXECUTE_ID.eq(search.getExecuteId()));

        if (StringUtils.isNotBlank(search.getCustomNumber()))
            conditions.add(TABLE.CUSTOM_NUMBER.like("%" + search.getCustomNumber().trim() + "%"));

        search.getDbTypeFields().forEach((k, v) -> {
            final Field<?> field = PDS_CUSTOM_INFO_TABLE.field(k);

            if (field == null) {
                logger.warn("invalid type: " + k);
            } else if (field.getType().equals(Date.class) || field.getType().equals(Timestamp.class)) {
                if (v.getStartDate() != null)
                    conditions.add(TABLE.field(k, Date.class).greaterOrEqual(v.getStartDate()));
                if (v.getEndDate() != null)
                    conditions.add(TABLE.field(k, Date.class).lessOrEqual(v.getEndDate()));
            } else if (k.contains("_INT_") || k.contains("_CONCODE_") || k.contains("_CSCODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(TABLE.field(k, String.class).eq(v.getKeyword()));
            } else if (k.contains("_STRING_") || k.contains("_NUMBER_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(TABLE.field(k, String.class).like("%" + v.getKeyword() + "%"));
            } else if (k.contains("_CODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getCode()))
                    conditions.add(TABLE.field(k, String.class).eq(v.getCode()));
            } else if (k.contains("_MULTICODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getCode()))
                    conditions.add(
                            TABLE.field(k, String.class).likeRegex("^" + v.getCode() + ",")
                                    .or(TABLE.field(k, String.class).likeRegex("^" + v.getCode() + "$"))
                                    .or(TABLE.field(k, String.class).likeRegex("," + v.getCode() + "$"))
                                    .or(TABLE.field(k, String.class).likeRegex("," + v.getCode() + ","))
                    );
            } else {
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(TABLE.field(k, String.class).eq(v.getKeyword()));
            }
        });

        return conditions;
    }
}
