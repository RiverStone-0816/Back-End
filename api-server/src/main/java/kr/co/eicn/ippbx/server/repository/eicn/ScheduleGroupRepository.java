package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ScheduleGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ContextInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroupList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScheduleGroupListEntity;
import kr.co.eicn.ippbx.server.model.enums.ScheduleKind;
import kr.co.eicn.ippbx.server.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import lombok.Getter;
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

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ContextInfo.CONTEXT_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.IvrTree.IVR_TREE;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ScheduleGroup.SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ScheduleGroupList.SCHEDULE_GROUP_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.SoundList.SOUND_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ScheduleGroupRepository extends EicnBaseRepository<ScheduleGroup, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ScheduleGroupRepository.class);
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;

    public ScheduleGroupRepository(PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(SCHEDULE_GROUP, SCHEDULE_GROUP.PARENT, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroup.class);

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
                            into.setKindDataName(ivrTreeMap.getOrDefault(into.getKindData(), EMPTY));
                        else if (ScheduleKind.EXCEPTION_CONTEXT.getCode().equals(into.getKind()))
                            into.setKindDataName(contextInfoMap.getOrDefault(into.getKindData(), EMPTY));

                        return into;
                    })
                    .collect(Collectors.toList())
            );

            scheduleGroupEntities.add(scheduleGroupEntity);
        });

        return scheduleGroupEntities;
    }

    public void insert(ScheduleGroupFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroup record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroup();
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
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        this.insert(pbxDsl, record);
                    }
                });
    }

    public void insertItem(DSLContext dslContext, ScheduleGroupList scheduleGroupList, Integer targetParent) {
        dslContext.insertInto(SCHEDULE_GROUP_LIST)
                .set(SCHEDULE_GROUP_LIST.CHILD_NAME, scheduleGroupList.getChildName())
                .set(SCHEDULE_GROUP_LIST.PARENT, targetParent)
                .set(SCHEDULE_GROUP_LIST.FROMHOUR, scheduleGroupList.getFromhour())
                .set(SCHEDULE_GROUP_LIST.TOHOUR, scheduleGroupList.getTohour())
                .set(SCHEDULE_GROUP_LIST.KIND, scheduleGroupList.getKind())
                .set(SCHEDULE_GROUP_LIST.KIND_DATA, scheduleGroupList.getKindData())
                .set(SCHEDULE_GROUP_LIST.KIND_SOUND_CODE, scheduleGroupList.getKindSoundCode())
                .set(SCHEDULE_GROUP_LIST.COMPANY_ID, scheduleGroupList.getCompanyId())
                .execute();
    }

    public void itemCopy(Integer parent, Integer targetParent) {
        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ScheduleGroup> scheduleGroups = findAll(SCHEDULE_GROUP.PARENT.eq(parent).or(SCHEDULE_GROUP.PARENT.eq(targetParent)));
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
                insertItem(dsl, scheduleGroupList, targetParent);
            }

            cacheService.pbxServerList(getCompanyId())
                    .forEach(e -> {
                        try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                            for (ScheduleGroupList scheduleGroupList : scheduleGroupLists) {
                                insertItem(pbxDsl, scheduleGroupList, targetParent);
                            }
                        }
                    });
        }
    }

    private List<SoundList> getSoundList() {
        return dsl.select()
                .from(SOUND_LIST)
                .where(SOUND_LIST.COMPANY_ID.eq(getCompanyId()))
                .fetchInto(SoundList.class);
    }

    private List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree> getIvrRootTree() {
        return dsl.select()
                .from(IVR_TREE)
                .where(IVR_TREE.COMPANY_ID.eq(getCompanyId()))
                .and(IVR_TREE.TYPE.eq((byte) 1))
                .and(IVR_TREE.LEVEL.eq(0))
                .orderBy(IVR_TREE.CODE)
                .fetchInto(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree.class);
    }

    private List<ContextInfo> getContextInfo() {
        return dsl.select()
                .from(CONTEXT_INFO)
                .where(CONTEXT_INFO.COMPANY_ID.eq(getCompanyId()))
                .orderBy(CONTEXT_INFO.NAME)
                .fetchInto(ContextInfo.class);
    }

    public void deleteAllPBXServer(Integer parent) {
        deleteAllPBXServer(dsl, parent);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        this.deleteAllPBXServer(pbxDsl, parent);
                    }
                });
    }

    public void deleteAllPBXServer(DSLContext dslContext, Integer parent) {
        dslContext.delete(SCHEDULE_GROUP)
                .where(SCHEDULE_GROUP.PARENT.eq(parent))
                .and(compareCompanyId())
                .execute();
    }
}
