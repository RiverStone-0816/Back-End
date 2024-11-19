package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupListEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleKind;
import kr.co.eicn.ippbx.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.CloseableDSLContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ContextInfo.CONTEXT_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.IvrTree.IVR_TREE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.LguCallbotInfo.LGU_CALLBOT_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroup.SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroupList.SCHEDULE_GROUP_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendMessageTemplate.SEND_MESSAGE_TEMPLATE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SoundList.SOUND_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ScheduleGroupRepository extends EicnBaseRepository<ScheduleGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ScheduleGroupRepository.class);
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;

    public ScheduleGroupRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(SCHEDULE_GROUP, SCHEDULE_GROUP.PARENT, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup.class);

        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    public List<ScheduleGroupEntity> getScheduleGroupLists() {
        final Map<Record, Result<Record>> recordResultMap = dsl.select()
                .from(SCHEDULE_GROUP)
                .leftOuterJoin(SCHEDULE_GROUP_LIST)
                .on(SCHEDULE_GROUP.PARENT.eq(SCHEDULE_GROUP_LIST.PARENT).and(SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId())))
                .where(compareCompanyId())
                .orderBy(SCHEDULE_GROUP.NAME.asc(), SCHEDULE_GROUP_LIST.TOHOUR.asc())
                .fetch()
                .intoGroups(SCHEDULE_GROUP.fields());

        final List<ScheduleGroupEntity> scheduleGroupEntities = new ArrayList<>();
        final Map<String, String> soundListMap = getSoundList().stream().collect(Collectors.toMap(e -> String.valueOf(e.getSeq()), SoundList::getSoundName));
        final Map<String, String> ivrTreeMap = getIvrRootTree().stream().collect(Collectors.toMap(e -> String.valueOf(e.getCode()), IvrTree::getName));
        final Map<String, String> contextInfoMap = getContextInfo().stream().collect(Collectors.toMap(ContextInfo::getContext, ContextInfo::getName));
        final Map<String, String> sendMessageTemplateMap = getSendMessageTemplate().stream().collect(Collectors.toMap(e -> String.valueOf(e.getId()), SendMessageTemplate::getContent));
        final Map<String, String> lguCallbotInfoMap = getLguCallbotInfo().stream().collect(Collectors.toMap(LguCallbotInfo::getCallbotKey, LguCallbotInfo::getServiceName));

        recordResultMap.forEach((record, records) -> {
            final ScheduleGroupEntity scheduleGroupEntity = record.into(ScheduleGroupEntity.class);

            scheduleGroupEntity.setScheduleGroupLists(records.stream()
                    .filter(r -> r.getValue(SCHEDULE_GROUP_LIST.CHILD) != null)
                    .map(r -> {
                        final ScheduleGroupListEntity into = r.into(ScheduleGroupListEntity.class);

                        if (isNotEmpty(into.getKindSoundCode()))
                            into.setKindSoundName(soundListMap.getOrDefault(into.getKindSoundCode(), EMPTY));
                        if (into.getKindSoundCode().equals("TTS"))
                            into.setKindSoundName("TTS");

                        if (ScheduleKind.IVR_CONNECT.getCode().equals(into.getKind()))
                            into.setKindDataName(ivrTreeMap.getOrDefault(into.getKindData(), "미확인 예외컨택스트"));
                        else if (ScheduleKind.EXCEPTION_CONTEXT.getCode().equals(into.getKind()))
                            into.setKindDataName(contextInfoMap.getOrDefault(into.getKindData(), "미확인 예외컨택스트"));
                        else if (ScheduleKind.EXCEPTION_CONTEXT_DIRECT_NUMBER.getCode().equals(into.getKind())) {
                            final String[] kindDataArray = into.getKindData().split("\\|");
                            final String context = kindDataArray.length > 0 ? contextInfoMap.getOrDefault(kindDataArray[0], "미확인 예외컨택스트") : "예외컨택스트 설정필요";
                            final String data = kindDataArray.length > 1 ? kindDataArray[1] : "번호 설정필요";
                            into.setKindDataName(context.concat(" | ").concat(data));
                        } else if (ScheduleKind.EXCEPTION_CONTEXT_IVR_CONNECT.getCode().equals(into.getKind())) {
                            final String[] kindDataArray = into.getKindData().split("\\|");
                            final String context = kindDataArray.length > 0 ? contextInfoMap.getOrDefault(kindDataArray[0], "미확인 예외컨택스트") : "예외컨택스트 설정필요";
                            final String data = kindDataArray.length > 1 ? ivrTreeMap.getOrDefault(kindDataArray[1], "미확인 IVR") : "설정필요";
                            into.setKindDataName(context.concat(" | ").concat(data));
                        } else if (ScheduleKind.SMS.getCode().equals(into.getKind())) {
                            into.setKindDataName(sendMessageTemplateMap.getOrDefault(into.getKindData(), "미확인 SMS 템플릿"));
                        } else if (ScheduleKind.CALLBOT.getCode().equals(into.getKind())) {
                            into.setKindDataName(lguCallbotInfoMap.getOrDefault(into.getKindData(), EMPTY));
                        }
                        return into;
                    })
                    .collect(Collectors.toList())
            );

            scheduleGroupEntities.add(scheduleGroupEntity);
        });

        return scheduleGroupEntities;
    }

    public void insert(ScheduleGroupFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup();
        ReflectionUtils.copy(record, form);

        record.setCompanyId(getCompanyId());

        Integer newKey = dsl.select(DSL.max(SCHEDULE_GROUP.PARENT))
                .from(SCHEDULE_GROUP)
                .orderBy(SCHEDULE_GROUP.PARENT.desc())
                .fetchOneInto(Integer.class);

        record.setParent(newKey != null ? newKey + 1 : 1);

        super.insert(record);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    this.insert(pbxDsl, record);
                });
    }

    public void update(ScheduleGroupFormRequest form, Integer parent) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup();
        ReflectionUtils.copy(record, form);
        record.setCompanyId(getCompanyId());

        super.updateByKey(record, parent);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    this.updateByKey(pbxDsl, record, parent);
                });
    }

    public Record insertItem(DSLContext dslContext, ScheduleGroupList scheduleGroupList, Integer targetParent, boolean chk) {
        if (chk)
            return dslContext.insertInto(SCHEDULE_GROUP_LIST)
                    .set(SCHEDULE_GROUP_LIST.CHILD_NAME, scheduleGroupList.getChildName())
                    .set(SCHEDULE_GROUP_LIST.PARENT, targetParent)
                    .set(SCHEDULE_GROUP_LIST.FROMHOUR, scheduleGroupList.getFromhour())
                    .set(SCHEDULE_GROUP_LIST.TOHOUR, scheduleGroupList.getTohour())
                    .set(SCHEDULE_GROUP_LIST.KIND, scheduleGroupList.getKind())
                    .set(SCHEDULE_GROUP_LIST.KIND_DATA, scheduleGroupList.getKindData())
                    .set(SCHEDULE_GROUP_LIST.KIND_SOUND_CODE, scheduleGroupList.getKindSoundCode())
                    .set(SCHEDULE_GROUP_LIST.COMPANY_ID, scheduleGroupList.getCompanyId())
                    .returning().fetchOne();

        dslContext.insertInto(SCHEDULE_GROUP_LIST)
                .set(SCHEDULE_GROUP_LIST.CHILD, scheduleGroupList.getChild())
                .set(SCHEDULE_GROUP_LIST.CHILD_NAME, scheduleGroupList.getChildName())
                .set(SCHEDULE_GROUP_LIST.PARENT, targetParent)
                .set(SCHEDULE_GROUP_LIST.FROMHOUR, scheduleGroupList.getFromhour())
                .set(SCHEDULE_GROUP_LIST.TOHOUR, scheduleGroupList.getTohour())
                .set(SCHEDULE_GROUP_LIST.KIND, scheduleGroupList.getKind())
                .set(SCHEDULE_GROUP_LIST.KIND_DATA, scheduleGroupList.getKindData())
                .set(SCHEDULE_GROUP_LIST.KIND_SOUND_CODE, scheduleGroupList.getKindSoundCode())
                .set(SCHEDULE_GROUP_LIST.COMPANY_ID, scheduleGroupList.getCompanyId())
                .execute();
        return null;
    }

    public void itemCopy(Integer parent, Integer targetParent) {
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup> scheduleGroups = findAll(SCHEDULE_GROUP.PARENT.eq(parent).or(SCHEDULE_GROUP.PARENT.eq(targetParent)));
        if (scheduleGroups.size() != 2)
            throw new IllegalArgumentException();

        final Integer count = dsl.select(DSL.count()).from(SCHEDULE_GROUP_LIST).where(SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId()).and(SCHEDULE_GROUP_LIST.PARENT.eq(targetParent))).fetchOne(0, int.class);

        if (count < 1) {
            final List<ScheduleGroupList> scheduleGroupLists = dsl.select()
                    .from(SCHEDULE_GROUP_LIST)
                    .where(SCHEDULE_GROUP_LIST.PARENT.eq(parent))
                    .and(SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId()))
                    .orderBy(SCHEDULE_GROUP_LIST.FROMHOUR)
                    .fetchInto(ScheduleGroupList.class);
            for (ScheduleGroupList scheduleGroupList : scheduleGroupLists) {
                Record record = insertItem(dsl, scheduleGroupList, targetParent, true);
                final ScheduleGroupList list = record.into(ScheduleGroupList.class);
                cacheService.pbxServerList(getCompanyId())
                        .forEach(e -> {
                            try (CloseableDSLContext pbxDsl = (CloseableDSLContext) pbxServerInterface.using(e.getHost())) {
                                insertItem(pbxDsl, list, targetParent, false);
                            }
                        });
            }
        }
    }

    private List<SoundList> getSoundList() {
        return dsl.select(SOUND_LIST.SEQ, SOUND_LIST.SOUND_NAME)
                .from(SOUND_LIST)
                .where(SOUND_LIST.COMPANY_ID.eq(getCompanyId()))
                .fetchInto(SoundList.class);
    }

    private List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree> getIvrRootTree() {
        return dsl.select(IVR_TREE.BUTTON, IVR_TREE.CODE, IVR_TREE.NAME)
                .from(IVR_TREE)
                .where(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                .and(IVR_TREE.TYPE.eq((byte) 1))
                .and(IVR_TREE.LEVEL.eq(0))
                .and(IVR_TREE.BUTTON.eq(EMPTY))
                .orderBy(IVR_TREE.CODE)
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree.class);
    }

    private List<ContextInfo> getContextInfo() {
        return dsl.select()
                .from(CONTEXT_INFO)
                .where(CONTEXT_INFO.COMPANY_ID.eq(getCompanyId()))
                .orderBy(CONTEXT_INFO.NAME)
                .fetchInto(ContextInfo.class);
    }

    private List<SendMessageTemplate> getSendMessageTemplate() {
        return dsl.select(SEND_MESSAGE_TEMPLATE.ID, SEND_MESSAGE_TEMPLATE.CONTENT)
                .from(SEND_MESSAGE_TEMPLATE)
                .where(SEND_MESSAGE_TEMPLATE.COMPANY_ID.eq(getCompanyId()))
                .fetchInto(SendMessageTemplate.class);
    }

    public void deleteAllPBXServer(Integer parent) {
        deleteAllPBXServer(dsl, parent);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    this.deleteAllPBXServer(pbxDsl, parent);
                });
    }

    public void deleteAllPBXServer(DSLContext dslContext, Integer parent) {
        dslContext.delete(SCHEDULE_GROUP)
                .where(SCHEDULE_GROUP.PARENT.eq(parent))
                .and(compareCompanyId())
                .execute();
    }

    public List<LguCallbotInfo> getLguCallbotInfo() {
        return dsl.select()
                .from(LGU_CALLBOT_INFO)
                .where(LGU_CALLBOT_INFO.COMPANY_ID.eq(getCompanyId()))
                .fetchInto(LguCallbotInfo.class);
    }
}
