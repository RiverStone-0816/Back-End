package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.ResultCustomInfoRecord;
import kr.co.eicn.ippbx.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PDSResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.pds.tables.ResultCustomInfo.RESULT_CUSTOM_INFO;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@Getter
public class PDSResultCustomInfoRepository extends PDSDbBaseRepository<CommonPDSResultCustomInfo, PDSResultCustomInfoEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PDSCustomInfoRepository.class);

    private final CommonPDSResultCustomInfo TABLE;
    private final CommonPDSCustomInfo customInfoTable;

    @Autowired
    private PDSCustomInfoService pdsCustomInfoService;
    @Autowired
    private CommonFieldRepository fieldRepository;

    /**
     * @param name {companyId}_{executeId}
     */
    public PDSResultCustomInfoRepository(String name) {
        super(new CommonPDSResultCustomInfo(name), new CommonPDSResultCustomInfo(name).SEQ, PDSResultCustomInfoEntity.class);
        this.TABLE = new CommonPDSResultCustomInfo(name);
        // pds_custom_info_eicn_28
        customInfoTable = new CommonPDSCustomInfo(substringBeforeLast(name, "_"));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(customInfoTable).on(customInfoTable.PDS_SYS_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, PDSResultCustomInfoEntity> getMapper() {
        return record -> {
            final PDSResultCustomInfoEntity entity = record.into(TABLE).into(PDSResultCustomInfoEntity.class);
            entity.setCustomInfo(record.into(customInfoTable).into(PdsCustomInfo.class));
            return entity;
        };
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
        Pagination<PDSResultCustomInfoEntity> pagination = super.pagination(search, conditions(search));

        List<PDSResultCustomInfoEntity> list = pagination.getRows();

        String pdsGroupId = "";
        if (!search.getExecuteId().equals(""))
            pdsGroupId = search.getExecuteId().substring(0, search.getExecuteId().lastIndexOf("_"));

        final List<PDSCustomInfoEntity> pdsCustomInfoFormRequestList = pdsCustomInfoService.getRepository(Integer.parseInt(pdsGroupId)).findAll();

        list.forEach(e -> {
            Optional<PDSCustomInfoEntity> any = pdsCustomInfoFormRequestList.stream().filter(custom -> custom.getPdsSysCustomId().equals(e.getCustomId())).findAny();
            any.ifPresent(e::setPdsCustomInfoEntity);

        });
        return new Pagination<>(list, pagination.getPage(), pagination.getTotalCount(), search.getLimit());
    }

    private List<Condition> conditions(PDSResultCustomInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(TABLE.EXECUTE_ID.eq(search.getExecuteId()));

        if (search.getCreatedStartDate() != null)
            conditions.add(DSL.date(TABLE.RESULT_DATE).greaterOrEqual(search.getCreatedStartDate()));

        if (search.getCreatedEndDate() != null)
            conditions.add(DSL.date(TABLE.RESULT_DATE).lessOrEqual(search.getCreatedEndDate()));

        if (search.getUserId() != null)
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        search.getDbTypeFields().forEach((k, v) -> {
            final Field<?> field = TABLE.field(k);

            if (field == null) {
                logger.warn("invalid type: " + k);
            } else if (field.getType().equals(Date.class) || field.getType().equals(Timestamp.class)) {
                if (v.getStartDate() != null)
                    conditions.add(DSL.cast(field, Date.class).greaterOrEqual(v.getStartDate()));
                if (v.getEndDate() != null)
                    conditions.add(DSL.cast(field, Date.class).lessOrEqual(v.getEndDate()));
            } else if (k.contains("_INT_") || k.contains("_CODE_") || k.contains("_CONCODE_") || k.contains("_CSCODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).eq(v.getKeyword()));
            } else if (k.contains("_STRING_") || k.contains("_NUMBER_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).like("%" + v.getKeyword() + "%"));
            } else if (k.contains("_MULTICODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(
                            DSL.cast(field, String.class).likeRegex("^" + v.getKeyword() + ",")
                                    .or(DSL.cast(field, String.class).likeRegex("^" + v.getKeyword() + "$"))
                                    .or(DSL.cast(field, String.class).likeRegex("," + v.getKeyword() + "$"))
                                    .or(DSL.cast(field, String.class).likeRegex("," + v.getKeyword() + ","))
                    );
            } else {
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).eq(v.getKeyword()));
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

            if (tableField.getType().equals(Date.class)) {
                query.set((Field<Date>) tableField, (Date) invoked);
            } else if (tableField.getType().equals(Timestamp.class)) {
                query.set((Field<Timestamp>) tableField, (Timestamp) invoked);
            } else if (tableField.getType().equals(Integer.class)) {
                query.set((Field<Integer>) tableField, (Integer) invoked);
            } else { // String.class
                query.set((Field<String>) tableField, (String) invoked);
            }
        }

        query
                .where(TABLE.SEQ.eq(seq))
                .execute();

    }

    //삭제
    public void deleteData(Integer seq) {
        //고객데이터삭제
        dsl.delete(TABLE)
                .where(TABLE.SEQ.eq(seq))
                .execute();
    }
}
