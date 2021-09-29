package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroupList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.ScheduleGroupListRecord;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListTimeUpdateFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.Getter;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroupList.SCHEDULE_GROUP_LIST;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.jooq.impl.DSL.ifnull;

@Getter
@Repository
public class ScheduleGroupListRepository extends EicnBaseRepository<ScheduleGroupList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ScheduleGroupListRepository.class);
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;

    ScheduleGroupListRepository(ScheduleGroupRepository scheduleGroupRepository, PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(SCHEDULE_GROUP_LIST, SCHEDULE_GROUP_LIST.CHILD, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList.class);
        this.scheduleGroupRepository = scheduleGroupRepository;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    public void insertAllPbxServers(ScheduleGroupListFormRequest form) {
        findAllDuplicateItem(form.getParent(), form.getFromhour(), form.getTohour());

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList();
        ReflectionUtils.copy(record, form);

        record.setChildName(EMPTY);
        record.setStatYn(defaultString(form.getStatYn(), "Y"));
        record.setWorktimeYn(defaultString(form.getWorktimeYn(), "Y"));
        record.setCompanyId(getCompanyId());
        record.setTtsData(!"TTS".equals(form.getKindSoundCode()) ? EMPTY : form.getTtsData());

        final ScheduleGroupListRecord pbxRecord = insertOnGeneratedKey(record).into(ScheduleGroupListRecord.class);
        pbxRecord.changed(true);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    this.insert(pbxDsl, pbxRecord);
                });
    }

    public void updateByKey(ScheduleGroupListFormRequest form, Integer key) {
        findAllDuplicateItem(form.getParent(), key, form.getFromhour(), form.getTohour());

        if (!"TTS".equals(form.getKindSoundCode()))
            form.setTtsData(EMPTY);
        super.updateByKey(form, key);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.updateByKey(pbxDsl, form, key);
                });
    }

    public void updateTimeByKey(ScheduleGroupListTimeUpdateFormRequest form, Integer key) {
        findAllDuplicateItem(form.getParent(), key, form.getFromhour(), form.getTohour());

        updateByKey(form, key);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.updateByKey(pbxDsl, form, key);
                });
    }



    public void deleteByKey(Integer child) {
        deleteOnIfNullThrow(child);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    pbxDsl.deleteFrom(SCHEDULE_GROUP_LIST)
                            .where(SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId()))
                            .and(SCHEDULE_GROUP_LIST.CHILD.eq(child))
                            .execute();
                });
    }

    public void deleteByParent(Integer parent) {
        deleteByParent(dsl, parent);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    deleteByParent(pbxDsl, parent);
                });
    }

    public void deleteByParent(DSLContext dslContext, Integer parent) {
        dslContext.deleteFrom(SCHEDULE_GROUP_LIST)
                .where(SCHEDULE_GROUP_LIST.COMPANY_ID.eq(getCompanyId()))
                .and(SCHEDULE_GROUP_LIST.PARENT.eq(parent))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList> findAllByGroupId(Integer groupId) {
        return findAll(SCHEDULE_GROUP_LIST.PARENT.eq(groupId), SCHEDULE_GROUP_LIST.FROMHOUR.asc());
    }

    public void findAllDuplicateItem(Integer parent, Integer fromHour, Integer toHour) {
        findAllDuplicateItem(parent, null, fromHour, toHour);
    }

    public void findAllDuplicateItem(Integer parent, Integer child, Integer fromHour, Integer toHour) {
        scheduleGroupRepository.findOneIfNullThrow(parent);
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroupList> duplicatedTime =
                findAll(SCHEDULE_GROUP_LIST.FROMHOUR.lt(fromHour).and(SCHEDULE_GROUP_LIST.TOHOUR.gt(fromHour))
                        .or(SCHEDULE_GROUP_LIST.FROMHOUR.lt(toHour).and(SCHEDULE_GROUP_LIST.TOHOUR.gt(toHour)))
                        .or(SCHEDULE_GROUP_LIST.FROMHOUR.ge(fromHour).and(SCHEDULE_GROUP_LIST.TOHOUR.le(toHour)))
                        .and(SCHEDULE_GROUP_LIST.PARENT.eq(parent))
                        .and(SCHEDULE_GROUP_LIST.CHILD.ne(child))
                );

        if (duplicatedTime.size() > 0)
            throw new DuplicateKeyException("이미 사용중인 시간이 있습니다. 시간을 다시 설정해 주세요.");
    }
}
