package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonEicnCdr;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbMultichannelInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.ResultCustomInfoRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentWtalkRoom;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbMultichannelInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.EicnCdrService;
import kr.co.eicn.ippbx.server.service.MaindbCustomInfoService;
import kr.co.eicn.ippbx.server.service.MaindbMultichannelInfoService;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MaindbGroup.MAINDB_GROUP;
import static org.apache.commons.lang3.StringUtils.replace;

@Getter
public class ResultCustomInfoRepository extends CustomDBBaseRepository<CommonResultCustomInfo, ResultCustomInfoEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ResultCustomInfoRepository.class);

    private final CommonResultCustomInfo TABLE;
    private final CommonMaindbCustomInfo MAINDB_CUSTOM_INFO_TABLE;
    private final kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbMultichannelInfo MAINDB_MULTICHANNEL_INFO_TABLE;

    @Autowired
    private MaindbMultichannelInfoService maindbMultichannelInfoService;
    @Autowired
    private CommonFieldRepository fieldRepository;
    @Autowired
    private MaindbGroupRepository maindbGroupRepository;
    @Autowired
    private MaindbCustomInfoService maindbCustomInfoService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TodoListRepository todoListRepository;
    @Autowired
    private CurrentWtalkRoomRepository currentWtalkRoomRepository;
    @Autowired
    private CurrentVocCustomListRepository vocCustomListRepository;
    @Autowired
    private EicnCdrService eicnCdrService;
    @Value("${file.path.custom}")
    private String savePath;

    public ResultCustomInfoRepository(String companyId) {
        super(new CommonResultCustomInfo(companyId), new CommonResultCustomInfo(companyId).SEQ, ResultCustomInfoEntity.class);
        TABLE = new CommonResultCustomInfo(companyId);
        MAINDB_CUSTOM_INFO_TABLE = new CommonMaindbCustomInfo(companyId);
        MAINDB_MULTICHANNEL_INFO_TABLE = new kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbMultichannelInfo(companyId);

        addField(TABLE);
        addField(MAINDB_CUSTOM_INFO_TABLE);
        addField(MAINDB_GROUP.GROUP_TREE_NAME);

        addOrderingField(TABLE.RESULT_DATE.desc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        query.groupBy(getSelectingFields());

        return query
                .join(MAINDB_CUSTOM_INFO_TABLE).on(MAINDB_CUSTOM_INFO_TABLE.MAINDB_SYS_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .join(MAINDB_GROUP).on(MAINDB_GROUP.SEQ.eq(TABLE.GROUP_ID))
                .leftJoin(MAINDB_MULTICHANNEL_INFO_TABLE).on(MAINDB_MULTICHANNEL_INFO_TABLE.MAINDB_CUSTOM_ID.eq(TABLE.CUSTOM_ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, ResultCustomInfoEntity> getMapper() {
        return record -> {
            final ResultCustomInfoEntity entity = record.into(TABLE).into(ResultCustomInfoEntity.class);
            entity.setCustomInfo(record.into(MAINDB_CUSTOM_INFO_TABLE).into(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo.class));

            return entity;
        };
    }

    @Override
    protected void postProcedure(List<ResultCustomInfoEntity> entities) {
        if (entities.size() == 0) return;

        final Map<String, List<MaindbMultichannelInfoEntity>> customIdToChannelInfoList = maindbMultichannelInfoService.getRepository().findAllByCustomIds(
                entities.stream()
                        .map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getCustomId)
                        .distinct()
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(CommonMaindbMultichannelInfo::getMaindbCustomId));
        entities.forEach(e -> e.setMultichannelList(customIdToChannelInfoList.get(e.getCustomId())));

        final Set<String> ids = new HashSet<>();
        ids.addAll(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getUserid).collect(Collectors.toList()));
        ids.addAll(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getUseridOrg).collect(Collectors.toList()));
        ids.addAll(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo::getUseridTr).collect(Collectors.toList()));

        final Map<String, String> userMap = userRepository.findAllByIds(ids).stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));

        for (ResultCustomInfoEntity entity : entities) {
            if (userMap.containsKey(entity.getUserid()))
                entity.setUserName(userMap.get(entity.getUserid()));
            else
                entity.setUserName(entity.getUserid());

            if (userMap.containsKey(entity.getUseridOrg()))
                entity.setUserOrgName(userMap.get(entity.getUseridOrg()));
            else
                entity.setUserOrgName(entity.getUseridOrg());

            if (userMap.containsKey(entity.getUseridTr()))
                entity.setUserTrName(userMap.get(entity.getUseridTr()));
            else
                entity.setUserTrName(entity.getUseridTr());
        }
    }

    public Pagination<ResultCustomInfoEntity> pagination(ResultCustomInfoSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<ResultCustomInfoEntity> getOne(ResultCustomInfoSearchRequest search) {
        return dsl.select()
                .from(TABLE)
                .where(TABLE.CLICK_KEY.eq(search.getClickKey() == null ? "" : search.getClickKey()))
                .fetchInto(ResultCustomInfoEntity.class);
    }

    public List<ResultCustomInfoEntity> getTodo(String userId, String phohe) {
        return dsl.select()
                .from(TABLE)
                .where(TABLE.USERID_TR.eq(userId).and(TABLE.CUSTOM_NUMBER.eq(phohe).and(TABLE.COMPANY_ID.eq(getCompanyId()))))
                .fetchInto(ResultCustomInfoEntity.class);
    }

    private List<Condition> conditions(ResultCustomInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    break;
                case MINE:
                    conditions.add(TABLE.USERID.eq(g.getUser().getId()));
                    break;
                case GROUP:
                    conditions.add(MAINDB_GROUP.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        conditions.add(TABLE.GROUP_KIND.ne("PHONE_TMP"));

        if (search.getSeq() != null)
            conditions.add(TABLE.GROUP_ID.eq(search.getSeq()));

        if (search.getCreatedStartDate() != null)
            conditions.add(DSL.date(TABLE.RESULT_DATE).ge(search.getCreatedStartDate()));

        if (search.getCreatedEndDate() != null)
            conditions.add(DSL.date(TABLE.RESULT_DATE).le(search.getCreatedEndDate()));

        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(TABLE.USERID.eq(search.getUserId()));

        if (StringUtils.isNotEmpty(search.getChannelData()) && search.getChannelType() != null) {
            if (search.getChannelType().equals("PHONE")) {
                conditions.add(MAINDB_MULTICHANNEL_INFO_TABLE.CHANNEL_TYPE.eq(search.getChannelType())
                        .and(MAINDB_MULTICHANNEL_INFO_TABLE.CHANNEL_DATA.replace("-", "").like("%" + search.getChannelData().replace("-", "") + "%")));
            } else {
                conditions.add(MAINDB_MULTICHANNEL_INFO_TABLE.CHANNEL_TYPE.eq(search.getChannelType())
                        .and(MAINDB_MULTICHANNEL_INFO_TABLE.CHANNEL_DATA.like("%" + search.getChannelData() + "%")));
            }
        }
        if (StringUtils.isNotEmpty(search.getClickKey()))
            conditions.add(TABLE.CLICK_KEY.eq(search.getClickKey()));

        search.getDbTypeFields().forEach((k, v) -> {
            final Field<?> field = TABLE.field(k) != null ? TABLE.field(k) : MAINDB_CUSTOM_INFO_TABLE.field(k);

            if (k.equals("CUSTOM_ID")) {
                conditions.add(MAINDB_CUSTOM_INFO_TABLE.MAINDB_SYS_CUSTOM_ID.eq(v.getKeyword()));
            } else if (field == null) {
                logger.warn("invalid type: " + k);
            } else if (field.getType().equals(Date.class) || field.getType().equals(Timestamp.class)) {
                if (v.getStartDate() != null)
                    conditions.add(DSL.cast(field, Date.class).greaterOrEqual(v.getStartDate()));
                if (v.getEndDate() != null)
                    conditions.add(DSL.cast(field, Date.class).lessOrEqual(v.getEndDate()));
            } else if (k.contains("_INT_") || k.contains("_CONCODE_") || k.contains("_CSCODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).eq(v.getKeyword()));
            } else if (k.contains("_STRING_") || k.contains("_NUMBER_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).like("%" + v.getKeyword() + "%"));
            } else if (k.contains("_MULTICODE_") || k.contains("_CODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
                if (StringUtils.isNotEmpty(v.getCode()))
                    conditions.add(
                            DSL.cast(field, String.class).likeRegex("^" + v.getCode() + ",")
                                    .or(DSL.cast(field, String.class).likeRegex("^" + v.getCode() + "$"))
                                    .or(DSL.cast(field, String.class).likeRegex("," + v.getCode() + "$"))
                                    .or(DSL.cast(field, String.class).likeRegex("," + v.getCode() + ","))
                    );
            } else {
                if (StringUtils.isNotEmpty(v.getKeyword()))
                    conditions.add(DSL.cast(field, String.class).eq(v.getKeyword()));
            }
        });
        return conditions;
    }

    public void update(ResultCustomInfoFormRequest form, ResultCustomInfoEntity resultCustomInfoEntity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final UpdateSetMoreStep<ResultCustomInfoRecord> query = dsl.update(TABLE)
                //.set(TABLE.MAINDB_SYS_GROUP_ID, form.getGroupSeq())
                //.set(TABLE.MAINDB_SYS_UPLOAD_DATE, DSL.now())
                .set(TABLE.RESULT_TYPE, form.getResultType())
                .set(TABLE.USERID, g.getUser().getId())
                .set(TABLE.USERID_TR, form.getUserIdTr())
                .set(TABLE.GROUP_KIND, form.getGroupKind().equals("PHONE_TMP") ? "PHONE" : form.getGroupKind())
                .set(TABLE.UPDATE_DATE, DSL.now())
                .set(TABLE.CUSTOM_ID, form.getCustomId())
                .set(TABLE.GROUP_ID, form.getGroupId())
                .set(TABLE.GROUP_TYPE, form.getMaindbType())
                .set(TABLE.CLICK_KEY, StringUtils.isEmpty(resultCustomInfoEntity.getClickKey()) ? "nonClickKey" : resultCustomInfoEntity.getClickKey());

        if (StringUtils.isNotEmpty(form.getCustomNumber()))
            query.set(TABLE.CUSTOM_NUMBER, form.getCustomNumber());

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
                if (StringUtils.isNotEmpty((String) invoked) && fieldName.contains("img")) {
                    final ResultCustomInfoEntity entity = findOne(TABLE.SEQ.eq(resultCustomInfoEntity.getSeq()));
                    final String oldFileName = fieldName.contains("1") ? entity.getRsImg_1() : (fieldName.contains("2") ? entity.getRsImg_2() : entity.getRsImg_3());
                    final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
                    maindbCustomInfoService.uploadImgWithFileStore((String) invoked, oldFileName);
                    query.set((Field<String>) tableField, path.toString() + "/" + (String) invoked);
                } else
                    query.set((Field<String>) tableField, (String) invoked);
            }
        }
        query
                .where(TABLE.SEQ.eq(resultCustomInfoEntity.getSeq()))
                .execute();

        form.setSeq(resultCustomInfoEntity.getSeq());
        if (StringUtils.isEmpty(form.getCustomNumber())) {
            final Optional<MaindbMultichannelInfoEntity> optionalChannelInfo = Optional.ofNullable(maindbMultichannelInfoService.getRepository().findAll(MAINDB_MULTICHANNEL_INFO_TABLE.MAINDB_CUSTOM_ID.eq(form.getCustomId()).and(MAINDB_MULTICHANNEL_INFO_TABLE.CHANNEL_TYPE.eq("PHONE"))).get(0));
            optionalChannelInfo.ifPresent(e -> form.setCustomNumber(e.getChannelData()));
        }

        if (form.getGroupKind() != null && form.getGroupKind().equals("TALK")) {
            currentWtalkRoomRepository.updateRoomNameByRoomId(form.getHangupMsg(), form.getRoomName());
        }

        if (StringUtils.isNotEmpty(form.getUserIdTr())) {
            todoListRepository.insertTransferData(form.getSeq(), form.getUserIdTr(), form.getCustomNumber()/*StringUtils.isNotEmpty(form.getCustomId()) ? form.getCustomId() : form.getCustomNumber()*/);
        }
    }

    public void insert(ResultCustomInfoFormRequest form) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        EicnCdrEntity eicnCdrEntity = null;
        if (StringUtils.isNotEmpty(form.getUniqueId())) {
            eicnCdrEntity = eicnCdrService.getRepository().findOne(new CommonEicnCdr(getCompanyId()).UNIQUEID.eq(form.getUniqueId()));
        }

        final InsertSetMoreStep<ResultCustomInfoRecord> query = dsl.insertInto(TABLE)
                .set(TABLE.RESULT_TYPE, form.getResultType())
                .set(TABLE.CALL_TYPE, StringUtils.isNotEmpty(form.getUniqueId()) && Objects.nonNull(eicnCdrEntity) ? eicnCdrEntity.getInOut() : form.getCallType())
                .set(TABLE.UNIQUEID, form.getUniqueId() == null ? "" : form.getUniqueId())
                .set(TABLE.CUSTOM_NUMBER, form.getCustomNumber() == null ? "" : form.getCustomNumber())
                .set(TABLE.CLICK_KEY, StringUtils.isEmpty(form.getClickKey()) ? "nonClickKey" : form.getClickKey())
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
                if (StringUtils.isNotEmpty((String) invoked) && fieldName.contains("img")) {
                    final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
                    maindbCustomInfoService.uploadImgWithFileStore((String) invoked, null);
                    query.set((Field<String>) tableField, path.toString() + "/" + (String) invoked);
                } else
                    query.set((Field<String>) tableField, (String) invoked);
            }
        }

        query.execute();

        if (StringUtils.isNotEmpty(form.getUserIdTr())) {
            todoListRepository.insertTransferData(form.getSeq(), form.getUserIdTr(), form.getCustomNumber());
        }

        if (form.getGroupKind().equals("TALK")) {
            currentWtalkRoomRepository.updateRoomNameByRoomId(form.getHangupMsg(), form.getRoomName());
        }

        if (Objects.nonNull(form.getVocGroup())) {
            vocCustomListRepository.insertByVocGroup(form);
        }
    }
}
