package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.records.PrvCustomInfoRecord;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.server.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.form.PrvCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.PrvCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PrvGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.UserRepository;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.customdb.tables.PrvCustomInfo.PRV_CUSTOM_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PrvGroup.PRV_GROUP;

@Getter
public class PrvCustomInfoRepository extends CustomDBBaseRepository<CommonPrvCustomInfo, PrvCustomInfoEntity, String> {
    protected final Logger logger = LoggerFactory.getLogger(MaindbCustomInfoRepository.class);

    private final CommonPrvCustomInfo TABLE;
    private final CommonResultCustomInfo resultTable;

    @Autowired
    private PrvGroupRepository prvGroupRepository;
    @Autowired
    private UserRepository userRepository;

    public PrvCustomInfoRepository(String companyId, Integer groupId) {
        super(createTable(companyId, groupId), createTable(companyId, groupId).PRV_SYS_CUSTOM_ID, PrvCustomInfoEntity.class);
        this.TABLE = createTable(companyId, groupId);
        this.resultTable = PrvResultCustomInfoRepository.createTable(companyId, groupId);

        addOrderingField(TABLE.PRV_SYS_UPLOAD_DATE.desc());
    }

    public static CommonPrvCustomInfo createTable(String companyId, Integer groupId) {
        if (StringUtils.isEmpty(companyId))
            throw new IllegalArgumentException("companyId is null");
        if (groupId == null)
            throw new IllegalArgumentException("groupId is null");

        return new CommonPrvCustomInfo(companyId + "_" + groupId);
    }

    public static String getTimeStr() {
        Calendar cal = Calendar.getInstance();
        return new java.text.DecimalFormat("00").format(cal.get(Calendar.YEAR)) +
                new java.text.DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1) +
                new java.text.DecimalFormat("00").format(cal.get(Calendar.DATE)) +
                new java.text.DecimalFormat("00").format(cal.get(Calendar.HOUR_OF_DAY)) +
                new java.text.DecimalFormat("00").format(cal.get(Calendar.MINUTE)) +
                new java.text.DecimalFormat("00").format(cal.get(Calendar.SECOND));
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
/**** 이전 join 문
        return query
                .leftJoin(resultTable).on(resultTable.CUSTOM_ID.eq(TABLE.PRV_SYS_CUSTOM_ID).and(TABLE.PRV_SYS_LAST_RESULT_ID.eq(resultTable.SEQ)))
                .where();
*/
        return query
                .leftJoin(resultTable).on(resultTable.CUSTOM_ID.eq(TABLE.PRV_SYS_CUSTOM_ID).and(TABLE.PRV_SYS_LAST_CALL_UNIQUEID.eq(resultTable.UNIQUEID)).and(resultTable.GROUP_KIND.eq("PHONE")))
                .where();
    }

    @Override
    protected RecordMapper<Record, PrvCustomInfoEntity> getMapper() {
        return record -> {
            final PrvCustomInfoEntity entity = record.into(TABLE.fields()).into(PrvCustomInfoEntity.class);
            final kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo customInfo = record.into(resultTable).into(kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo.class);
            if (customInfo != null && customInfo.getSeq() != null)
                entity.setResult(customInfo);
            return entity;
        };
    }

    @Override
    protected void postProcedure(List<PrvCustomInfoEntity> entities) {
        userRepository.findAllByIds(entities.stream().map(kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonPrvCustomInfo::getPrvSysDamdangId).filter(Objects::nonNull).collect(Collectors.toSet()))
                .forEach(e -> entities.stream().filter(e2 -> Objects.equals(e.getId(), e2.getPrvSysDamdangId())).forEach(e2 -> e2.setPrvSysDamdangName(e.getIdName())));
    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE.getName())
                .columns(PRV_CUSTOM_INFO.fields())
                .constraint(DSL.constraint(TABLE.PRV_SYS_CUSTOM_ID.getName()).primaryKey(TABLE.PRV_SYS_CUSTOM_ID.getName()))
                .indexes(PRV_CUSTOM_INFO.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }

    public Pagination<PrvCustomInfoEntity> pagination(PrvCustomInfoSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(PrvCustomInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getGroupSeq() != null)
            conditions.add(TABLE.PRV_SYS_GROUP_ID.eq(search.getGroupSeq()));

        if (search.getCreatedStartDate() != null)
            conditions.add(DSL.date(TABLE.PRV_SYS_UPLOAD_DATE).ge(search.getCreatedStartDate()));

        if (search.getCreatedEndDate() != null)
            conditions.add(DSL.date(TABLE.PRV_SYS_UPLOAD_DATE).le(search.getCreatedEndDate()));

        if (search.getLastResultStartDate() != null)
            conditions.add(DSL.date(TABLE.PRV_SYS_LAST_RESULT_DATE).ge(search.getLastResultStartDate()));

        if (search.getLastResultEndDate() != null)
            conditions.add(DSL.date(TABLE.PRV_SYS_LAST_RESULT_DATE).le(search.getLastResultEndDate()));

        if (StringUtils.isNotEmpty(search.getPersonIdInCharge()))
            conditions.add(TABLE.PRV_SYS_DAMDANG_ID.eq(search.getPersonIdInCharge()));

        search.getDbTypeFields().forEach((k, v) -> {
            final Field<?> field = TABLE.field(k) != null ? TABLE.field(k) : resultTable.field(k);

            if (field == null) {
                logger.warn("invalid type: " + k);
            } else if (field.getType().equals(Date.class) || field.getType().equals(Timestamp.class)) {
                if (v.getStartDate() != null)
                    conditions.add(DSL.cast(field, Date.class).greaterOrEqual(v.getStartDate()));
                if (v.getEndDate() != null)
                    conditions.add(DSL.cast(field, Date.class).lessOrEqual(v.getEndDate()));
            } else if (k.contains("_INT_") || k.contains("_CODE_") || k.contains("_CONCODE_") || k.contains("_CSCODE_")) {
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).eq(v.getKeyword()));
            } else if (k.contains("_STRING_") || k.contains("_NUMBER_")) {
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).like("%" + v.getKeyword() + "%"));
            } else if (k.contains("_MULTICODE_")) {
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

    @SuppressWarnings("unchecked")
    public void insert(PrvCustomInfoFormRequest form) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final PrvGroup prvGroup = prvGroupRepository.findOne(form.getGroupSeq());

        final String id = "PRV_" + form.getGroupSeq() + getTimeStr();

        final InsertSetMoreStep<PrvCustomInfoRecord> query = dsl.insertInto(TABLE)
                .set(TABLE.PRV_SYS_CUSTOM_ID, id)
                .set(TABLE.PRV_SYS_GROUP_ID, form.getGroupSeq())
                .set(TABLE.PRV_SYS_UPLOAD_DATE, DSL.now())
                .set(TABLE.PRV_SYS_UPDATE_DATE, DSL.now())
                .set(TABLE.PRV_SYS_LAST_RESULT_DATE, DSL.now())
                .set(TABLE.PRV_SYS_LAST_CALL_DATE, DSL.now())
                .set(TABLE.PRV_SYS_GROUP_TYPE, prvGroup.getPrvType())
                .set(TABLE.PRV_SYS_DAMDANG_ID, form.getDamdangId())
                .set(TABLE.PRV_SYS_RESULT_TYPE, prvGroup.getResultType())
                .set(TABLE.PRV_SYS_DAMDANG_ID, g.getUser().getId())
                .set(TABLE.PRV_SYS_COMPANY_ID, getCompanyId());

        final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
        for (java.lang.reflect.Field field : form.getClass().getDeclaredFields()) {
            if (!insertableFieldTypes.contains(field.getType()))
                continue;

            final String fieldName = field.getName();
            final Field<?> tableField = TABLE.field("PRV_" + fieldName.toUpperCase());
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

        query.execute();

        dsl.update(PRV_GROUP)
                .set(PRV_GROUP.TOTAL_CNT, PRV_GROUP.TOTAL_CNT.add(1))
                .where(PRV_GROUP.SEQ.eq(prvGroup.getSeq()))
                .execute();
    }

    @SuppressWarnings("unchecked")
    public void update(PrvCustomInfoFormRequest form, String id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final UpdateSetMoreStep<PrvCustomInfoRecord> query = dsl.update(TABLE)
                .set(TABLE.PRV_SYS_UPDATE_DATE, DSL.now())
                .set(TABLE.PRV_SYS_DAMDANG_ID, g.getUser().getId());

        final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
        for (java.lang.reflect.Field field : form.getClass().getDeclaredFields()) {
            if (!insertableFieldTypes.contains(field.getType()))
                continue;

            final String fieldName = field.getName();
            final Field<?> tableField = TABLE.field("PRV_" + fieldName.toUpperCase());
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

        query.where(TABLE.PRV_SYS_CUSTOM_ID.eq(id)).execute();
    }

    public void deleteData(String id) {
        final PrvCustomInfoEntity entity = findOneIfNullThrow(id);

        delete(entity.getPrvSysCustomId());

        // TODO: 안좋은 코드. 다른 connector를 무단으로 사용
        dsl.update(PRV_GROUP)
                .set(PRV_GROUP.TOTAL_CNT, PRV_GROUP.TOTAL_CNT.minus(1))
                .where(PRV_GROUP.SEQ.eq(entity.getPrvSysGroupId()))
                .execute();
    }

    public void updateLastResult(String customId, Integer resultId, Timestamp resultDate, Timestamp callDate, String callUniqueId, String hangupCause) {
        dsl.update(TABLE)
                .set(TABLE.PRV_SYS_LAST_RESULT_ID, resultId)
                .set(TABLE.PRV_SYS_LAST_RESULT_DATE, resultDate)
                .set(TABLE.PRV_SYS_LAST_CALL_DATE, callDate)
                .set(TABLE.PRV_SYS_LAST_CALL_UNIQUEID, callUniqueId)
                .set(TABLE.PRV_SYS_LAST_HANGUP_CAUSE, hangupCause)
                .where(TABLE.PRV_SYS_CUSTOM_ID.eq(customId))
                .and(TABLE.PRV_SYS_LAST_RESULT_ID.isNull().or(TABLE.PRV_SYS_LAST_RESULT_ID.le(resultId)))
                .execute();
    }

    public void redistribution(List<String> customIdList, List<String> userIdList) {
        final List<Query> queries = new ArrayList<>();

        for (int i = 0; i < customIdList.size(); i++) {
            queries.add(
                    DSL.update(TABLE)
                            .set(TABLE.PRV_SYS_DAMDANG_ID, userIdList.get(i % userIdList.size()))
                            .where(TABLE.PRV_SYS_CUSTOM_ID.eq(customIdList.get(i)))
            );
        }

        dsl.batch(queries).execute();
    }
}
