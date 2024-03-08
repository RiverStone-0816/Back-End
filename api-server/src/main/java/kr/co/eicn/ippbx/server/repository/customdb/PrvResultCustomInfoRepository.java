package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ResultCustomInfoRecord;
import kr.co.eicn.ippbx.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.UserEntity;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PrvResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.UserRepository;
import kr.co.eicn.ippbx.server.service.PrvCustomInfoService;
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
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PrvResultCustomInfoRepository extends CustomDBBaseRepository<CommonResultCustomInfo, PrvResultCustomInfoEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PrvResultCustomInfoRepository.class);

    private final String companyId;
    private final Integer groupId;
    private final CommonResultCustomInfo TABLE;
    private final CommonPrvCustomInfo customInfoTable;

    @Autowired
    private PrvCustomInfoService prvCustomInfoService;
    @Autowired
    private UserRepository userRepository;

    public PrvResultCustomInfoRepository(String companyId, Integer groupId) {
        super(createTable(companyId, groupId), createTable(companyId, groupId).SEQ, PrvResultCustomInfoEntity.class);
        this.companyId = companyId;
        this.groupId = groupId;
        this.TABLE = createTable(companyId, groupId);
        this.customInfoTable = PrvCustomInfoRepository.createTable(companyId, groupId);

        addOrderingField(TABLE.RESULT_DATE.desc());
    }

    public static CommonResultCustomInfo createTable(String companyId, Integer groupId) {
        if (StringUtils.isEmpty(companyId))
            throw new IllegalArgumentException("companyId is null");
        if (groupId == null)
            throw new IllegalArgumentException("groupId is null");

        return new CommonResultCustomInfo(companyId + "_" + groupId + "_prv");
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(customInfoTable).on(customInfoTable.PRV_SYS_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, PrvResultCustomInfoEntity> getMapper() {
        return record -> {
            final PrvResultCustomInfoEntity entity = record.into(TABLE.fields()).into(PrvResultCustomInfoEntity.class);
            entity.setCustomInfo(record.into(customInfoTable.fields()).into(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonPrvCustomInfo.class));
            return entity;
        };
    }

    @Override
    protected void postProcedure(List<PrvResultCustomInfoEntity> entities) {
        if (entities.size() == 0) return;

        final Set<String> ids = new HashSet<>();
        ids.addAll(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getUserid).collect(Collectors.toList()));
        ids.addAll(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getUseridOrg).collect(Collectors.toList()));
        ids.addAll(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getUseridTr).collect(Collectors.toList()));

        for (UserEntity user : userRepository.findAllByIds(ids)) {
            for (PrvResultCustomInfoEntity entity : entities) {
                if (Objects.equals(entity.getUserid(), user.getId()))
                    entity.setUserName(user.getIdName());
                if (Objects.equals(entity.getUseridOrg(), user.getId()))
                    entity.setUserOrgName(user.getIdName());
                if (Objects.equals(entity.getUseridTr(), user.getId()))
                    entity.setUserTrName(user.getIdName());
            }
        }

    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE)
                .columns(TABLE.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(TABLE.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }

    public Pagination<PrvResultCustomInfoEntity> pagination(PrvResultCustomInfoSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(PrvResultCustomInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(TABLE.GROUP_KIND.ne("PHONE_TMP"));

        if (search.getGroupSeq() != null)
            conditions.add(TABLE.GROUP_ID.eq(search.getGroupSeq()));

        if (search.getCreatedStartDate() != null)
            conditions.add(TABLE.RESULT_DATE.ge(DSL.timestamp(search.getCreatedStartDate() + " 00:00:00")));

        if (search.getCreatedEndDate() != null)
            conditions.add(TABLE.RESULT_DATE.le(DSL.timestamp(search.getCreatedEndDate() + " 23:59:59")));

        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        if (StringUtils.isNotEmpty(search.getClickKey()))
            conditions.add(TABLE.CLICK_KEY.eq(search.getClickKey()));

        search.getDbTypeFields().forEach((k, v) -> {
            final Field<?> field = TABLE.field(k) != null ? TABLE.field(k) : customInfoTable.field(k);

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

    public void update(ResultCustomInfoFormRequest form, Integer seq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final UpdateSetMoreStep<ResultCustomInfoRecord> query = dsl.update(TABLE)
                .set(TABLE.RESULT_TYPE, form.getResultType())
                .set(TABLE.USERID, g.getUser().getId())
                .set(TABLE.UPDATE_DATE, DSL.now())
                .set(TABLE.GROUP_KIND, "PHONE")
                .set(TABLE.USERID_ORG, g.getUser().getId())
                .set(TABLE.GROUP_TYPE, form.getMaindbType())
                .set(TABLE.RESULT_TYPE, form.getResultType());

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

        query.where(TABLE.SEQ.eq(seq))
                .execute();

        final PrvResultCustomInfoEntity result = findOne(seq);

        prvCustomInfoService.getRepository(groupId).updateLastResult(
                result.getCustomId(),
                result.getSeq(),
                result.getResultDate(),
                result.getResultDate(),
                result.getUniqueid(),
                result.getHangupCause()
        );
    }

    public void insert(ResultCustomInfoFormRequest form) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final InsertSetMoreStep<ResultCustomInfoRecord> query = dsl.insertInto(TABLE)
                .set(TABLE.RESULT_TYPE, form.getResultType())
                .set(TABLE.CALL_TYPE, form.getCallType())
                .set(TABLE.UNIQUEID, form.getUniqueId())
                .set(TABLE.CUSTOM_NUMBER, form.getCustomNumber())
                .set(TABLE.CLICK_KEY, form.getClickKey())
                .set(TABLE.FROM_ORG, form.getFromOrg())
                .set(TABLE.GROUP_KIND, form.getGroupKind())
                .set(TABLE.GROUP_ID, form.getGroupId())
                .set(TABLE.CUSTOM_ID, form.getCustomId())
                .set(TABLE.GROUP_TYPE, form.getMaindbType())
                .set(TABLE.USERID, g.getUser().getId())
                .set(TABLE.USERID_ORG, g.getUser().getId())
                .set(TABLE.USERID_TR, form.getUserIdTr())
                .set(TABLE.COMPANY_ID, getCompanyId())
                .set(TABLE.HANGUP_CAUSE, form.getHangupCause())
                .set(TABLE.HANGUP_MSG, form.getHangupMsg())
                .set(TABLE.BILLSEC, form.getBillSec())
                .set(TABLE.RESULT_DATE, DSL.now())
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

        query.execute();

        if (StringUtils.isNotEmpty(form.getUniqueId())) { // TODO: cannot get returning from dynamic table
            final List<PrvResultCustomInfoEntity> results = findAll(TABLE.UNIQUEID.eq(form.getUniqueId()));

            if (!results.isEmpty()) {
                final PrvResultCustomInfoEntity result = results.get(results.size() - 1);

                prvCustomInfoService.getRepository(groupId).updateLastResult(
                        result.getCustomId(),
                        result.getSeq(),
                        result.getResultDate(),
                        result.getResultDate(),
                        result.getUniqueid(),
                        result.getHangupCause()
                );
            }
        }
    }
}
