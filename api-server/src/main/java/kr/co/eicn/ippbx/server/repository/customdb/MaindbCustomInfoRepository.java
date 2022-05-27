package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.CommonRoutines;
import kr.co.eicn.ippbx.meta.jooq.customdb.routines.FnEncStringText;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.MaindbKeyInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbMultichannelInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.MaindbCustomInfoRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbCustomFieldResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbMultichannelInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.UserEntity;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.MaindbDataSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.MaindbCustomInfoService;
import kr.co.eicn.ippbx.server.service.MaindbKeyInfoService;
import kr.co.eicn.ippbx.server.service.MaindbMultichannelInfoService;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.*;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.jooq.impl.DSL.noCondition;

@Getter
public class MaindbCustomInfoRepository extends CustomDBBaseRepository<CommonMaindbCustomInfo, MaindbCustomInfoEntity, String> {
    protected final Logger logger = LoggerFactory.getLogger(MaindbCustomInfoRepository.class);

    private final CommonMaindbCustomInfo TABLE;
    private final kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbMultichannelInfo multichannelInfoTable;
    @Autowired
    private MaindbMultichannelInfoService maindbMultichannelInfoService;
    @Autowired
    private CommonFieldRepository fieldRepository;
    @Autowired
    private MaindbGroupRepository maindbGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MaindbKeyInfoService maindbKeyInfoService;
    @Autowired
    private CurrentWtalkRoomRepository currentWtalkRoomRepository;
    @Autowired
    private MaindbCustomInfoService maindbCustomInfoService;
    @Value("${file.path.custom}")
    private String savePath;

    public MaindbCustomInfoRepository(String companyId) {
        super(new CommonMaindbCustomInfo(companyId), new CommonMaindbCustomInfo(companyId).MAINDB_SYS_CUSTOM_ID, MaindbCustomInfoEntity.class);
        TABLE = new CommonMaindbCustomInfo(companyId);
        multichannelInfoTable = new kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbMultichannelInfo(companyId);

        addField(TABLE.MAINDB_SYS_CUSTOM_ID, TABLE.MAINDB_SYS_UPLOAD_DATE, TABLE.MAINDB_SYS_UPDATE_DATE,
                TABLE.MAINDB_SYS_GROUP_ID, TABLE.MAINDB_SYS_GROUP_TYPE, TABLE.MAINDB_SYS_RESULT_TYPE,
                TABLE.MAINDB_SYS_DAMDANG_ID, TABLE.MAINDB_SYS_LAST_RESULT_ID, TABLE.MAINDB_SYS_LAST_RESULT_DATE,
                TABLE.MAINDB_SYS_COMPANY_ID,
                TABLE.MAINDB_DATE_1, TABLE.MAINDB_DATE_2, TABLE.MAINDB_DATE_3,
                TABLE.MAINDB_DAY_1, TABLE.MAINDB_DAY_2, TABLE.MAINDB_DAY_3,
                TABLE.MAINDB_DATETIME_1, TABLE.MAINDB_DATETIME_2, TABLE.MAINDB_DATETIME_3,
                TABLE.MAINDB_INT_1, TABLE.MAINDB_INT_2, TABLE.MAINDB_INT_3, TABLE.MAINDB_INT_4, TABLE.MAINDB_INT_5,
                TABLE.MAINDB_STRING_1, TABLE.MAINDB_STRING_2, TABLE.MAINDB_STRING_3, TABLE.MAINDB_STRING_4, TABLE.MAINDB_STRING_5,
                TABLE.MAINDB_STRING_6, TABLE.MAINDB_STRING_7, TABLE.MAINDB_STRING_8, TABLE.MAINDB_STRING_9, TABLE.MAINDB_STRING_10,
                TABLE.MAINDB_STRING_11, TABLE.MAINDB_STRING_12, TABLE.MAINDB_STRING_13, TABLE.MAINDB_STRING_14, TABLE.MAINDB_STRING_15,
                CommonRoutines.fnDecStringText(TABLE.MAINDB_STRING_16, "eicn_" + companyId).as(TABLE.MAINDB_STRING_16),
                CommonRoutines.fnDecStringText(TABLE.MAINDB_STRING_17, "eicn_" + companyId).as(TABLE.MAINDB_STRING_17),
                CommonRoutines.fnDecStringText(TABLE.MAINDB_STRING_18, "eicn_" + companyId).as(TABLE.MAINDB_STRING_18),
                CommonRoutines.fnDecStringText(TABLE.MAINDB_STRING_19, "eicn_" + companyId).as(TABLE.MAINDB_STRING_19),
                CommonRoutines.fnDecStringText(TABLE.MAINDB_STRING_20, "eicn_" + companyId).as(TABLE.MAINDB_STRING_20),
                TABLE.MAINDB_CODE_1, TABLE.MAINDB_CODE_2, TABLE.MAINDB_CODE_3, TABLE.MAINDB_CODE_4, TABLE.MAINDB_CODE_5,
                TABLE.MAINDB_CODE_6, TABLE.MAINDB_CODE_7, TABLE.MAINDB_CODE_8, TABLE.MAINDB_CODE_9, TABLE.MAINDB_CODE_10,
                TABLE.MAINDB_MULTICODE_1, TABLE.MAINDB_MULTICODE_2, TABLE.MAINDB_MULTICODE_3,
                TABLE.MAINDB_INT_1, TABLE.MAINDB_INT_2, TABLE.MAINDB_INT_3,
                TABLE.MAINDB_CONCODE_1, TABLE.MAINDB_CONCODE_2, TABLE.MAINDB_CONCODE_3,
                TABLE.MAINDB_CSCODE_1, TABLE.MAINDB_CSCODE_2, TABLE.MAINDB_CSCODE_3
        );
        addField(MAINDB_GROUP);
        addField(COMMON_TYPE);

        addField(CommonRoutines.fnDecStringText(TABLE.MAINDB_STRING_16.getName(), "eicn_" + getCompanyId()).as("MAINDB_STRING_16"));
        addOrderingField(TABLE.MAINDB_SYS_UPLOAD_DATE.desc());
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
        query.groupBy(getSelectingFields());

        return query
                .join(MAINDB_GROUP).on(MAINDB_GROUP.SEQ.eq(TABLE.MAINDB_SYS_GROUP_ID))
                .join(COMMON_TYPE).on(COMMON_TYPE.SEQ.eq(MAINDB_GROUP.MAINDB_TYPE))
                .where();
    }

    @Override
    protected RecordMapper<Record, MaindbCustomInfoEntity> getMapper() {
        return record -> {
            final MaindbCustomInfoEntity entity = record.into(TABLE).into(MaindbCustomInfoEntity.class);
            entity.setGroup(record.into(MAINDB_GROUP).into(MaindbGroup.class));
            entity.setType(record.into(COMMON_TYPE).into(CommonType.class));
            return entity;
        };
    }

    @Override
    protected void postProcedure(List<MaindbCustomInfoEntity> entities) {
        if (entities.size() == 0) return;

        final Integer typeSeq = entities.get(0).getType().getSeq();
        final List<CommonField> fields = fieldRepository.findAllCommonField(typeSeq);
        entities.forEach(e -> e.setFields(fields));

        final List<MaindbMultichannelInfoEntity> multichannelLists = maindbMultichannelInfoService.getRepository().findAllByCustomIds(
                entities.stream() // entities list
                        .map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo::getMaindbSysCustomId) // convert to custom id list
                        .distinct() // remove duplicated custom id
                        .collect(Collectors.toList()) // bind to list
        );

        Map<String, List<MaindbMultichannelInfoEntity>> customIdToChannelInfoList = multichannelLists.stream().collect(Collectors.groupingBy(CommonMaindbMultichannelInfo::getMaindbCustomId));
        if (customIdToChannelInfoList.size() > 0) {
            entities.forEach(e -> e.setMultichannelList(customIdToChannelInfoList.get(e.getMaindbSysCustomId())));
        }

        for (UserEntity user : userRepository.findAllByIds(entities.stream().map(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo::getMaindbSysDamdangId).collect(Collectors.toSet()))) {
            for (MaindbCustomInfoEntity entity : entities) {
                if (Objects.equals(entity.getMaindbSysDamdangId(), user.getId()))
                    entity.setMaindbSysDamdangName(user.getIdName());
            }
        }
    }

    public List<MaindbCustomFieldResponse> getType(Integer type) {
        return dsl.select()
                .from(COMMON_FIELD)
                .where(COMMON_FIELD.COMPANY_ID.eq(getCompanyId()))
                .and(COMMON_FIELD.TYPE.eq(type))
                .fetchInto(MaindbCustomFieldResponse.class);
    }

    public Pagination<MaindbCustomInfoEntity> pagination(MaindbDataSearchRequest search, boolean isCounselling) {
        return super.pagination(search, conditions(search, isCounselling));
    }

    private List<Condition> conditions(MaindbDataSearchRequest search, boolean isCounselling) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getGroupSeq() != null)
            conditions.add(TABLE.MAINDB_SYS_GROUP_ID.eq(search.getGroupSeq()));
        if (search.getCreatedStartDate() != null)
            conditions.add(DSL.date(TABLE.MAINDB_SYS_UPLOAD_DATE).greaterOrEqual(search.getCreatedStartDate()));
        if (search.getCreatedEndDate() != null)
            conditions.add(DSL.date(TABLE.MAINDB_SYS_UPLOAD_DATE).lessOrEqual(search.getCreatedEndDate()));

        Condition multichannelCondition = noCondition();

        if (search.getChannelType() != null)
            multichannelCondition = multichannelInfoTable.CHANNEL_TYPE.eq(search.getChannelType().getCode());
        if (StringUtils.isNotEmpty(search.getChannelData())) {
            if (isCounselling) {
                if (search.getChannelType() != null && search.getChannelType().equals(MultichannelChannelType.PHONE)) {
                    multichannelCondition = multichannelCondition.and(multichannelInfoTable.CHANNEL_DATA.replace("-", "").eq(search.getChannelData().replace("-", "")));
                } else {
                    multichannelCondition = multichannelCondition.and(multichannelInfoTable.CHANNEL_DATA.eq(search.getChannelData()));
                }
            } else {
                if (search.getChannelType() != null && search.getChannelType().equals(MultichannelChannelType.PHONE)) {
                    multichannelCondition = multichannelCondition.and(multichannelInfoTable.CHANNEL_DATA.replace("-", "").like("%" + search.getChannelData().replace("-", "") + "%"));
                } else {
                    multichannelCondition = multichannelCondition.and(multichannelInfoTable.CHANNEL_DATA.like("%" + search.getChannelData() + "%"));
                }
            }
        }

        if (!multichannelCondition.equals(noCondition()))
            conditions.add(TABLE.MAINDB_SYS_CUSTOM_ID.in(dsl.select(multichannelInfoTable.field(multichannelInfoTable.MAINDB_CUSTOM_ID))
                    .from(multichannelInfoTable)
                    .where(multichannelCondition)));

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

    public String insert(MaindbCustomInfoFormRequest form) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final MaindbGroup maindbGroup = maindbGroupRepository.findOne(form.getGroupSeq());

        final String id = form.getGroupSeq() + "_" + g.getUser().getId() + "_" + getTimeStr();
        final InsertSetMoreStep<MaindbCustomInfoRecord> query = dsl.insertInto(TABLE)
                .set(TABLE.MAINDB_SYS_CUSTOM_ID, id)
                .set(TABLE.MAINDB_SYS_GROUP_ID, form.getGroupSeq())
                .set(TABLE.MAINDB_SYS_UPLOAD_DATE, DSL.now())
                .set(TABLE.MAINDB_SYS_UPDATE_DATE, DSL.now())
                .set(TABLE.MAINDB_SYS_DAMDANG_ID, g.getUser().getId())
                .set(TABLE.MAINDB_SYS_COMPANY_ID, getCompanyId())
                .set(TABLE.MAINDB_SYS_GROUP_TYPE, maindbGroup.getMaindbType())
                .set(TABLE.MAINDB_SYS_RESULT_TYPE, maindbGroup.getResultType());

        final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
        FnEncStringText fnEncStringText = new FnEncStringText();


        for (Class<?> aClass = form.getClass(); !aClass.equals(Object.class); aClass = aClass.getSuperclass()) {
            for (java.lang.reflect.Field field : aClass.getDeclaredFields()) {
                if (!insertableFieldTypes.contains(field.getType()))
                    continue;

                final String fieldName = field.getName();
                final Field<?> tableField = TABLE.field("MAINDB_" + fieldName.toUpperCase());
                if (tableField == null)
                    continue;

                final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                final Object invoked = aClass.getMethod("get" + capName).invoke(form);

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
                    } else {
                        if (fieldName.contains("string_") && Integer.parseInt(fieldName.replace("string_", "")) > 15) {
                            query.set((Field<String>) tableField, CommonRoutines.fnEncStringText((String) invoked, "eicn_" + getCompanyId()));
                        } else {
                            query.set((Field<String>) tableField, (String) invoked);
                        }
                    }
                }
            }
        }


        final String customName = form.getString_1() != null ? form.getString_1() : "";

        query.execute();
        if (form.getChannels() != null) {
            for (MaindbCustomInfoFormRequest.ChannelForm channel : form.getChannels()) {
                maindbMultichannelInfoService.getRepository().insert(id, customName, channel);
                if (channel.getType().equals(MultichannelChannelType.TALK)) {
                    currentWtalkRoomRepository.updateGroupIdCustomIdCustomName(form.getGroupSeq(), id, customName, form.getRoomId());
                }
            }
            maindbMultichannelInfoService.getRepository().updateCustomName(id, customName);
        }

        if (maindbGroup.getIsDupUse() != null && maindbGroup.getIsDupUse().equals("Y")) {
            maindbKeyInfoService.getRepository().deleteByCustomId(id);
            String keyValue = form.getGroupSeq() + "$";

            if (maindbGroup.getDupKeyKind().equals("NUM")) {
                if (form.getChannels() != null) {
                    for (MaindbCustomInfoFormRequest.ChannelForm channel : form.getChannels()) {
                        keyValue = keyValue + "$$" + channel.getValue();
                        List<MaindbKeyInfo> list = maindbKeyInfoService.getRepository().selectOne(keyValue, form.getGroupSeq());
                        if (list.size() == 0)
                            maindbKeyInfoService.getRepository().insert(id, keyValue, form.getGroupSeq());
                    }
                }
            } else {
                String fieldName = maindbGroup.getDupNeedField().substring("MAINDB_".length()).toLowerCase();
                final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                final Object invoked = form.getClass().getMethod("get" + capName).invoke(form);

                if (maindbGroup.getDupKeyKind().equals("FLD")) {
                    keyValue = keyValue + "$$" + invoked;
                    List<MaindbKeyInfo> list = maindbKeyInfoService.getRepository().selectOne(keyValue, form.getGroupSeq());
                    if (list.size() == 0)
                        maindbKeyInfoService.getRepository().insert(id, keyValue, form.getGroupSeq());
                } else if (maindbGroup.getDupKeyKind().equals("FLD_NUM")) {
                    if (form.getChannels() != null) {
                        for (MaindbCustomInfoFormRequest.ChannelForm channel : form.getChannels()) {
                            keyValue = keyValue + "$$" + invoked + "$$" + channel.getValue();
                            List<MaindbKeyInfo> list = maindbKeyInfoService.getRepository().selectOne(keyValue, form.getGroupSeq());
                            if (list.size() == 0)
                                maindbKeyInfoService.getRepository().insert(id, keyValue, form.getGroupSeq());
                        }
                    }
                }
            }
        }

        return id;
    }

    public void update(MaindbCustomInfoFormRequest form, String id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final MaindbGroup maindbGroup = maindbGroupRepository.findOne(form.getGroupSeq());
        //final String id = "CST_" + form.getGroupSeq() + getTimeStr();
        final UpdateSetMoreStep<MaindbCustomInfoRecord> query = dsl.update(TABLE)
                //.set(TABLE.MAINDB_SYS_GROUP_ID, form.getGroupSeq())
                //.set(TABLE.MAINDB_SYS_UPLOAD_DATE, DSL.now())
                .set(TABLE.MAINDB_SYS_UPDATE_DATE, DSL.now())
                .set(TABLE.MAINDB_SYS_DAMDANG_ID, g.getUser().getId());
        //.set(TABLE.MAINDB_SYS_COMPANY_ID, getCompanyId())
        //.set(TABLE.MAINDB_SYS_GROUP_TYPE, maindbGroup.getMaindbType())
        //.set(TABLE.MAINDB_SYS_RESULT_TYPE, maindbGroup.getResultpe());

        final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
        for (Class<?> aClass = form.getClass(); !aClass.equals(Object.class); aClass = aClass.getSuperclass()) {
            for (java.lang.reflect.Field field : aClass.getDeclaredFields()) {
                if (!insertableFieldTypes.contains(field.getType()))
                    continue;

                final String fieldName = field.getName();
                final Field<?> tableField = TABLE.field("MAINDB_" + fieldName.toUpperCase());
                if (tableField == null)
                    continue;

                final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                final Object invoked = aClass.getMethod("get" + capName).invoke(form);

                if (tableField.getType().equals(Date.class)) {
                    query.set((Field<Date>) tableField, (Date) invoked);
                } else if (tableField.getType().equals(Timestamp.class)) {
                    query.set((Field<Timestamp>) tableField, (Timestamp) invoked);
                } else if (tableField.getType().equals(Integer.class)) {
                    query.set((Field<Integer>) tableField, (Integer) invoked);
                } else { // String.class
                    if (StringUtils.isNotEmpty((String) invoked) && fieldName.contains("img")) {
                        final MaindbCustomInfoEntity entity = findOne(TABLE.MAINDB_SYS_CUSTOM_ID.eq(id));
                        final String oldFileName = fieldName.contains("1") ? entity.getMaindbImg_1() : (fieldName.contains("2") ? entity.getMaindbImg_2() : entity.getMaindbImg_3());
                        final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
                        maindbCustomInfoService.uploadImgWithFileStore((String) invoked, oldFileName);
                        query.set((Field<String>) tableField, path.toString() + "/" + (String) invoked);
                    } else {
                        if (fieldName.contains("string_") && Integer.parseInt(fieldName.replace("string_", "")) > 15) {
                            query.set((Field<String>) tableField, CommonRoutines.fnEncStringText((String) invoked, "eicn_" + getCompanyId()));
                        } else {
                            query.set((Field<String>) tableField, (String) invoked);
                        }
                    }
                }
            }
        }

        final String customName = form.getString_1() != null ? form.getString_1() : "";

        query.where(TABLE.MAINDB_SYS_CUSTOM_ID.eq(id))
                .execute();


        final List<MaindbMultichannelInfoEntity> multichannelInfoEntities = maindbMultichannelInfoService.getRepository().findAllByCustomIds(Collections.singletonList(id));

        final Set<MaindbCustomInfoFormRequest.ChannelForm> previousChannelForms = multichannelInfoEntities.stream().map(e -> new MaindbCustomInfoFormRequest.ChannelForm(MultichannelChannelType.valueOf(e.getChannelType()), e.getChannelData())).collect(Collectors.toSet());
//        final Collection<MaindbCustomInfoFormRequest.ChannelForm> subtract = CollectionUtils.subtract(previousChannelForms, form.getChannels());
//
//        final Collection<MaindbCustomInfoFormRequest.ChannelForm> deleting = CollectionUtils.intersection(subtract, previousChannelForms);
//        final Collection<MaindbCustomInfoFormRequest.ChannelForm> inserting = CollectionUtils.intersection(subtract, form.getChannels());
//
//        for (MaindbCustomInfoFormRequest.ChannelForm channel : inserting) {
//            maindbMultichannelInfoService.getRepository().insert(id, customName, channel);
//            if (channel.getType().equals(MultichannelChannelType.TALK)) {
//                currentTalkRoomRepository.updateGroupIdCustomIdCustomName(form.getGroupSeq(), id, customName, form.getRoomId());
//            }
//        }
//
//        for (MaindbCustomInfoFormRequest.ChannelForm channel : deleting) {
//            maindbMultichannelInfoService.getRepository().deleteByTypeAndData(channel.getType(), channel.getValue());
//        }
//        //delete
//        final ArrayList<MaindbCustomInfoFormRequest.ChannelForm> deleteChannelList = new ArrayList<MaindbCustomInfoFormRequest.ChannelForm>();
//        for (MaindbCustomInfoFormRequest.ChannelForm preChannel : previousChannelForms) {
//            boolean isDelete = true;
//            final MaindbCustomInfoFormRequest.ChannelForm deleteChannel = new MaindbCustomInfoFormRequest.ChannelForm();
//            for (MaindbCustomInfoFormRequest.ChannelForm postChannel : form.getChannels()) {
//                if(preChannel.getType().equals(postChannel.getType()) && preChannel.getValue().equals(postChannel.getValue())) {
//                    isDelete = false;
//                }
//            }
//            if(isDelete) {
//                deleteChannel.setType(preChannel.getType());
//                deleteChannel.setValue(preChannel.getValue());
//                deleteChannelList.add(deleteChannel);
//            }
//        }
//
//        //insert
//        final ArrayList<MaindbCustomInfoFormRequest.ChannelForm> insertChannelList = new ArrayList<MaindbCustomInfoFormRequest.ChannelForm>();
//        for (MaindbCustomInfoFormRequest.ChannelForm formChannel : form.getChannels()) {
//            boolean isInsert = true;
//            final MaindbCustomInfoFormRequest.ChannelForm insertChannel = new MaindbCustomInfoFormRequest.ChannelForm();
//            for (MaindbCustomInfoFormRequest.ChannelForm preChannel : previousChannelForms) {
//                if(preChannel.getType().equals(formChannel.getType()) && preChannel.getValue().equals(formChannel.getValue())) {
//                    isInsert = false;
//                }
//            }
//            if(isInsert) {
//                insertChannel.setType(formChannel.getType());
//                insertChannel.setValue(formChannel.getValue());
//                insertChannelList.add(insertChannel);
//            }
//        }
//        for (MaindbCustomInfoFormRequest.ChannelForm channel : insertChannelList) {
//            maindbMultichannelInfoService.getRepository().insert(id, customName, channel);
//        }
//
//        if(form.getRoomId() != null && !form.getRoomId().equals("")) {
//            currentTalkRoomRepository.updateGroupIdCustomIdCustomName(form.getGroupSeq(), id, customName, form.getRoomId());
//        }
//
//        for (MaindbCustomInfoFormRequest.ChannelForm channel : deleteChannelList) {
//            maindbMultichannelInfoService.getRepository().deleteByTypeAndData(channel.getType(), channel.getValue());
//        }
//
//        maindbMultichannelInfoService.getRepository().updateCustomName(id, customName);

        if (form.getChannels() != null) {
            maindbMultichannelInfoService.getRepository().deleteByCustomId(id);
            for (MaindbCustomInfoFormRequest.ChannelForm channel : form.getChannels()) {
                maindbMultichannelInfoService.getRepository().insert(id, customName, channel);
                if (channel.getType().equals(MultichannelChannelType.TALK)) {
                    currentWtalkRoomRepository.updateGroupIdCustomIdCustomName(form.getGroupSeq(), id, customName, form.getRoomId());
                }
            }
            maindbMultichannelInfoService.getRepository().updateCustomName(id, customName);
        }

        if (maindbGroup.getIsDupUse() != null && maindbGroup.getIsDupUse().equals("Y")) {
            maindbKeyInfoService.getRepository().deleteByCustomId(id);
            String keyValue = form.getGroupSeq() + "$";

            if (maindbGroup.getDupKeyKind().equals("NUM")) {
                if (form.getChannels() != null) {
                    for (MaindbCustomInfoFormRequest.ChannelForm channel : form.getChannels()) {
                        keyValue = keyValue + "$$" + channel.getValue();
                        List<MaindbKeyInfo> list = maindbKeyInfoService.getRepository().selectOne(keyValue, form.getGroupSeq());
                        if (list.size() == 0)
                            maindbKeyInfoService.getRepository().insert(id, keyValue, form.getGroupSeq());
                    }
                }
            } else {
                String fieldName = maindbGroup.getDupNeedField().substring("MAINDB_".length()).toLowerCase();
                final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                final Object invoked = form.getClass().getMethod("get" + capName).invoke(form);

                if (maindbGroup.getDupKeyKind().equals("FLD")) {
                    keyValue = keyValue + "$$" + invoked;
                    List<MaindbKeyInfo> list = maindbKeyInfoService.getRepository().selectOne(keyValue, form.getGroupSeq());
                    if (list.size() == 0)
                        maindbKeyInfoService.getRepository().insert(id, keyValue, form.getGroupSeq());
                } else if (maindbGroup.getDupKeyKind().equals("FLD_NUM")) {
                    if (form.getChannels() != null) {
                        for (MaindbCustomInfoFormRequest.ChannelForm channel : form.getChannels()) {
                            keyValue = keyValue + "$$" + invoked + "$$" + channel.getValue();
                            List<MaindbKeyInfo> list = maindbKeyInfoService.getRepository().selectOne(keyValue, form.getGroupSeq());
                            if (list.size() == 0)
                                maindbKeyInfoService.getRepository().insert(id, keyValue, form.getGroupSeq());
                        }
                    }
                }
            }
        }
    }

    public void deleteData(String id) {
        //고객데이터삭제
        delete(id);
        //멀티정보삭제
        maindbMultichannelInfoService.getRepository().deleteByCustomId(id);
    }

    public MaindbCustomInfoService.customInfo getCustomImgPathById(String id) {
        return dsl.select(TABLE.MAINDB_IMG_1)
                .select(TABLE.MAINDB_IMG_2)
                .select(TABLE.MAINDB_IMG_3)
                .from(TABLE)
                .where(TABLE.MAINDB_SYS_CUSTOM_ID.eq(id))
                .fetchOneInto(MaindbCustomInfoService.customInfo.class);
    }
}
