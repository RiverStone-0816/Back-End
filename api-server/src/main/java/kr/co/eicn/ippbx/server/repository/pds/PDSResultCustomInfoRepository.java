package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.ResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.ResultCustomInfoRecord;
import kr.co.eicn.ippbx.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.UserEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PDSResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.UserRepository;
import kr.co.eicn.ippbx.server.service.PDSCustomInfoService;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.pds.tables.ResultCustomInfo.RESULT_CUSTOM_INFO;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@Getter
public class PDSResultCustomInfoRepository extends PDSDbBaseRepository<CommonPDSResultCustomInfo, PDSResultCustomInfoEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PDSCustomInfoRepository.class);

    private final CommonPDSResultCustomInfo TABLE;
    private final CommonPDSCustomInfo       PDS_CUSTOM_INFO_TABLE;

    @Autowired
    private PDSCustomInfoService  pdsCustomInfoService;
    @Autowired
    private CommonFieldRepository fieldRepository;
    @Autowired
    private PersonListRepository  personListRepository;

    /**
     * @param name {companyId}_{executeId}
     */
    public PDSResultCustomInfoRepository(String name) {
        super(new CommonPDSResultCustomInfo(name), new CommonPDSResultCustomInfo(name).SEQ, PDSResultCustomInfoEntity.class);
        this.TABLE = new CommonPDSResultCustomInfo(name);
        PDS_CUSTOM_INFO_TABLE = new CommonPDSCustomInfo(substringBeforeLast(name, "_"));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(PDS_CUSTOM_INFO_TABLE).on(PDS_CUSTOM_INFO_TABLE.PDS_SYS_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, PDSResultCustomInfoEntity> getMapper() {
        return record -> {
            final PDSResultCustomInfoEntity entity = record.into(TABLE).into(PDSResultCustomInfoEntity.class);
            entity.setCustomInfo(record.into(PDS_CUSTOM_INFO_TABLE).into(PDSCustomInfoEntity.class));
            return entity;
        };
    }

    @Override
    protected void postProcedure(List<PDSResultCustomInfoEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) return;

        final Map<String, String> userMap = personListRepository.getIdAndNameMap();
        for (PDSResultCustomInfoEntity entity : entities) {
            entity.setUserName(userMap.getOrDefault(entity.getUserid(), entity.getUserid()));
        }
    }

    public void createTableIfNotExists() {
        createTableIfNotExists(dsl);
    }

    public void createTableIfNotExists(DSLContext dslContext) {
        dslContext.createTableIfNotExists(TABLE)
                .columns(RESULT_CUSTOM_INFO.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(RESULT_CUSTOM_INFO.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }

    public Pagination<PDSResultCustomInfoEntity> pagination(PDSResultCustomInfoSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(PDSResultCustomInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(TABLE.EXECUTE_ID.eq(search.getExecuteId()));

        if (search.getCreatedStartDate() != null)
            conditions.add(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getCreatedStartDate() + " 00:00:00")));

        if (search.getCreatedEndDate() != null)
            conditions.add(TABLE.RESULT_DATE.le(DSL.timestamp(search.getCreatedEndDate() + " 23:59:59")));

        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        search.getDbTypeFields().forEach((k, v) -> {
            final Table<?> table = k.startsWith("PDS") ? PDS_CUSTOM_INFO_TABLE : TABLE;
            final Field<?> field = table.field(k);

            if (field == null) {
                logger.warn("invalid type: " + k);
            } else if (field.getType().equals(Date.class) || field.getType().equals(Timestamp.class)) {
                if (v.getStartDate() != null)
                    conditions.add(table.field(k, Date.class).greaterOrEqual(v.getStartDate()));
                if (v.getEndDate() != null)
                    conditions.add(table.field(k, Date.class).lessOrEqual(v.getEndDate()));
            } else if (k.contains("_INT_") || k.contains("_CONCODE_") || k.contains("_CSCODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(table.field(k, String.class).eq(v.getKeyword()));
            } else if (k.contains("_STRING_") || k.contains("_NUMBER_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(table.field(k, String.class).like("%" + v.getKeyword() + "%"));
            } else if (k.contains("_CODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getCode()))
                    conditions.add(table.field(k, String.class).eq(v.getCode()));
            } else if (k.contains("_MULTICODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getCode()))
                    conditions.add(
                            table.field(k, String.class).likeRegex("^" + v.getCode() + ",")
                                    .or(table.field(k, String.class).likeRegex("^" + v.getCode() + "$"))
                                    .or(table.field(k, String.class).likeRegex("," + v.getCode() + "$"))
                                    .or(table.field(k, String.class).likeRegex("," + v.getCode() + ","))
                    );
            } else {
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(table.field(k, String.class).eq(v.getKeyword()));
            }
        });

        return conditions;
    }

    public void update(PDSResultCustomInfoFormRequest form, Integer seq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final UpdateSetMoreStep<ResultCustomInfoRecord> query = dsl.update(TABLE)
                //.set(TABLE.MAINDB_SYS_GROUP_ID, form.getGroupSeq())
                //.set(TABLE.MAINDB_SYS_UPLOAD_DATE, DSL.now())
                .set(TABLE.USERID, g.getUser().getId())
                .set(TABLE.UPDATE_DATE, DSL.now());

        final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
        for (java.lang.reflect.Field field : form.getClass().getDeclaredFields()) {
            if (!insertableFieldTypes.contains(field.getType()))
                continue;

            final String fieldName = field.getName();
            final Field<?> tableField = TABLE.field("RS_" + fieldName.toUpperCase());
            if (tableField == null)
                continue;

            final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            final Object invoked = form.getClass().getMethod("get" + capName).invoke(form);

            if (tableField.getType().equals(Date.class))
                query.set((Field<Date>) tableField, (Date) invoked);
            else if (tableField.getType().equals(Timestamp.class))
                query.set((Field<Timestamp>) tableField, (Timestamp) invoked);
            else if (tableField.getType().equals(Integer.class))
                query.set((Field<Integer>) tableField, (Integer) invoked);
            else
                query.set((Field<String>) tableField, (String) invoked);
        }

        query
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }

    public void deleteData(Integer seq) {
        dsl.delete(TABLE)
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }
}
