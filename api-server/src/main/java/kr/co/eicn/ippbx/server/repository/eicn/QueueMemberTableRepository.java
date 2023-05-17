package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.Tables;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueMemberTable;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.QueueMemberTableRecord;
import kr.co.eicn.ippbx.model.dto.eicn.DashHuntMonitorResponse;
import kr.co.eicn.ippbx.model.dto.eicn.DashQueueMemberResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PDSQueuePersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.QueueMemberLoginCountResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CenterMemberStatusCountEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.MemberStatusOfHunt;
import kr.co.eicn.ippbx.model.enums.LicenseListType;
import kr.co.eicn.ippbx.model.enums.PersonPausedStatus;
import kr.co.eicn.ippbx.model.form.MonitControlChangeRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.QUEUE_NAME;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.jooq.impl.DSL.*;

@Getter
@Repository
public class QueueMemberTableRepository extends EicnBaseRepository<QueueMemberTable, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable, UInteger> {
    protected final Logger logger = LoggerFactory.getLogger(QueueMemberTableRepository.class);

    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;
    private final PersonListRepository personListRepository;
    private Boolean groupBy = false;

    public QueueMemberTableRepository(PBXServerInterface pbxServerInterface, CacheService cacheService, PersonListRepository personListRepository) {
        super(QUEUE_MEMBER_TABLE, QUEUE_MEMBER_TABLE.UNIQUEID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable.class);
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
        this.personListRepository = personListRepository;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        if (groupBy)
            query.groupBy(QUEUE_MEMBER_TABLE.MEMBERNAME);

        groupBy = false;
        return query.where();
    }

    public void deleteByKeyMemberNameAllPbxServers(String memberName) {
        dsl.deleteFrom(Tables.QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(memberName))
                .execute();

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            pbxDsl.deleteFrom(Tables.QUEUE_MEMBER_TABLE)
                    .where(compareCompanyId())
                    .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(memberName))
                    .execute();
        });
    }

    public Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> findByMemberName(DSLContext dslContext, String peer) {
        return dslContext.selectFrom(Tables.QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq("").or(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(peer)).or(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq("QUEUE".concat(peer))))
                .and(Tables.QUEUE_MEMBER_TABLE.MEMBERNAME.eq(peer))
                .fetch()
                .intoMap(Tables.QUEUE_MEMBER_TABLE.QUEUE_NAME, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> findAllPDSMember() {
        return findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.like("PDS%"));
    }

    public void insertAllPbxServers(Object record) {
        this.insert(dsl, record);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.insert(pbxDsl, record);
                });
    }

    public int deleteAllPbxServers(Condition condition) {
        final int r = super.delete(condition);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    super.delete(pbxDsl, condition);
                });

        return r;
    }

    public void insertOnConflictDoNothingAllPbxServers(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable record) {
        insertOnConflictDoNothing(dsl, record);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    insertOnConflictDoNothing(pbxDsl, record);
                });
    }

    public void insertOnConflictDoNothing(DSLContext dslContext, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable record) {
        Result<Record> result = dslContext.select(QUEUE_MEMBER_TABLE.fields())
                .from(QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(record.getQueueName()))
                .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(record.getMembername()))
                .fetch();

        if (result.size() == 0)
            dslContext.insertInto(Tables.QUEUE_MEMBER_TABLE)
                    .set(dslContext.newRecord(table, record))
                    .execute();
    }

    public void deleteByQueueNameAndMemberName(DSLContext dslContext, String queueName, String memberName) {
        dslContext.deleteFrom(Tables.QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(Tables.QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName).and(Tables.QUEUE_MEMBER_TABLE.MEMBERNAME.eq(memberName)))
                .execute();
    }

    public void updateQueueNameAndMemberName(DSLContext dslContext, QueueMemberTableRecord record) {
        dslContext.update(Tables.QUEUE_MEMBER_TABLE)
                .set(record)
                .where(compareCompanyId(Tables.QUEUE_MEMBER_TABLE))
                .and(Tables.QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(record.getQueueName()).and(Tables.QUEUE_MEMBER_TABLE.MEMBERNAME.eq(record.getMembername())))
                .execute();
    }

    public Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> findByOneQueueMemberTableQueueNameAndMemberName(DSLContext dslContext, String memberName) {
        return findAll(dslContext, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(memberName).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq("QUEUE" + memberName))).stream().findFirst();
    }

    public void updatePause(MonitControlChangeRequest form) {

        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        updatePause(dsl, form);
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            updatePause(pbxDsl, form);
        });
    }

    public void updatePause(DSLContext dslContext, MonitControlChangeRequest form) {
        dslContext.update(QUEUE_MEMBER_TABLE)
                .set(QUEUE_MEMBER_TABLE.PAUSED, PersonPausedStatus.valueOf(form.getPaused()).getCode())
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(EMPTY))
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(form.getPeer()))
                .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(form.getPeer()))
                .and(QUEUE_MEMBER_TABLE.PAUSED.notEqual(100))
                .execute();
    }

    public void updatePeerByUserId(PersonList record) {
        updateUserId(dsl, record.getId(), record.getPeer());
        updatePeerByUserId(dsl, record);
        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
                    updateUserId(pbxDsl, record.getId(), record.getPeer());
                    updatePeerByUserId(pbxDsl, record);
                });
    }

    public void updateUserId(DSLContext dslContext, String userId, String peer) {
        dslContext.update(QUEUE_MEMBER_TABLE)
                .set(QUEUE_MEMBER_TABLE.USERID, (String) null)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.startsWith("QUEUE"))
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.ne("QUEUE" + QUEUE_MEMBER_TABLE.MEMBERNAME))
                .and(QUEUE_MEMBER_TABLE.USERID.eq(userId))
                .execute();

        if (StringUtils.isNotEmpty(peer)) {
            dslContext.update(QUEUE_MEMBER_TABLE)
                    .set(QUEUE_MEMBER_TABLE.USERID, userId)
                    .where(compareCompanyId())
                    .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.startsWith("QUEUE"))
                    .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.ne("QUEUE" + QUEUE_MEMBER_TABLE.MEMBERNAME))
                    .and(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(peer))
                    .execute();
        }
    }

    public void updatePeerByUserId(DSLContext dslContext, PersonList record) {
        dslContext.update(QUEUE_MEMBER_TABLE)
                .set(QUEUE_MEMBER_TABLE.MEMBERNAME, when(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(record.getPeer()), record.getPeer()).else_(record.getId()))
                .set(QUEUE_MEMBER_TABLE.INTERFACE, StringUtils.isNotEmpty(record.getPeer()) ? "SIP/".concat(record.getPeer()) : "SIP/")
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.USERID.eq(record.getId()))
                .execute();
    }

    public void deleteByQueueNameBlending(DSLContext dslContext, String queueName) {
        dslContext.deleteFrom(QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                .and(QUEUE_MEMBER_TABLE.BLENDING_MODE.notEqual("N"))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> getBlendingPersons(String queueName) {
        return findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName).and(QUEUE_MEMBER_TABLE.BLENDING_MODE.notEqual("N")));
    }

    public DashHuntMonitorResponse getDashHuntMonitor(String queueName) {
        DashHuntMonitorResponse res = dsl.select(DSL.count(DSL.when(QUEUE_MEMBER_TABLE.IS_LOGIN.eq("Y").and(QUEUE_MEMBER_TABLE.PAUSED.eq(0)), 1)).as("counselWaitCnt"))
                .select(DSL.count(DSL.when(QUEUE_MEMBER_TABLE.IS_LOGIN.eq("N").and(QUEUE_MEMBER_TABLE.PAUSED.eq(0)), 1)).as("counselWaitNoLoginCnt"))
                .select(DSL.count(DSL.when(QUEUE_MEMBER_TABLE.PAUSED.eq(1), 1)).as("callingCnt"))
                .select(DSL.count(DSL.when(QUEUE_MEMBER_TABLE.PAUSED.notEqual(0).and(QUEUE_MEMBER_TABLE.PAUSED.notEqual(1)), 1)).as("etcCnt"))
                .select(DSL.count(DSL.when(QUEUE_MEMBER_TABLE.IS_LOGIN.eq("Y"), 1)).as("loginCnt"))
                .from(QUEUE_MEMBER_TABLE)
                .where(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                .and(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .fetchOneInto(DashHuntMonitorResponse.class);
        res.setQueueName(queueName);
        res.setCustomWaitCnt(dsl.select(sum(QUEUE_NAME.CNT))
                .from(QUEUE_NAME)
                .where(QUEUE_NAME.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(queueName.equals("") ? DSL.noCondition() : QUEUE_NAME.NAME.eq(queueName))
                .fetchOneInto(Integer.class));
        res.setRateValue(EicnUtils.getRateValue(0, 0));

        final List<DashQueueMemberResponse> queueMemberList = dsl.select(QUEUE_MEMBER_TABLE.MEMBERNAME)
                .select(QUEUE_MEMBER_TABLE.PAUSED)
                .select(QUEUE_MEMBER_TABLE.IS_LOGIN)
                .from(QUEUE_MEMBER_TABLE)
                .where(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                .and(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .fetch(r -> {
                    DashQueueMemberResponse member = new DashQueueMemberResponse();

                    member.setPeer(r.getValue(QUEUE_MEMBER_TABLE.MEMBERNAME));
                    member.setStatus(r.getValue(QUEUE_MEMBER_TABLE.PAUSED));
                    member.setLogin(Objects.equals(r.getValue(QUEUE_MEMBER_TABLE.IS_LOGIN), "Y"));

                    return member;
                });
        res.setQueueMemberList(queueMemberList);

        return res;
    }

    public List<QueueMemberLoginCountResponse> getIsLoginByQueueName() {
        return dsl.select(QUEUE_MEMBER_TABLE.QUEUE_NAME,
                        QUEUE_MEMBER_TABLE.IS_LOGIN,
                        count(QUEUE_MEMBER_TABLE.IS_LOGIN).as("count"))
                .from(QUEUE_MEMBER_TABLE)
                .leftJoin(PERSON_LIST)
                .on(QUEUE_MEMBER_TABLE.USERID.eq(PERSON_LIST.ID))
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.like("QUEUE%"))
                .and(PERSON_LIST.LICENSE_LIST.like("%" + LicenseListType.STAT.getCode() + "%"))
                .groupBy(QUEUE_MEMBER_TABLE.QUEUE_NAME, QUEUE_MEMBER_TABLE.IS_LOGIN)
                .fetchInto(QueueMemberLoginCountResponse.class);
    }

    public List<MemberStatusOfHunt> getStatusCountOfHunt() {
        return dsl.select(QUEUE_NAME.NAME, QUEUE_MEMBER_TABLE.PAUSED, count(QUEUE_MEMBER_TABLE.PAUSED).as("count"))
                .from(QUEUE_MEMBER_TABLE)
                .innerJoin(QUEUE_NAME).on(QUEUE_NAME.NAME.eq(QUEUE_MEMBER_TABLE.QUEUE_NAME))
                .innerJoin(PERSON_LIST).on(PERSON_LIST.ID.eq(QUEUE_MEMBER_TABLE.USERID))
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(""))
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(QUEUE_MEMBER_TABLE.MEMBERNAME))
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(DSL.field("'QUEUE'+{0}", String.class, QUEUE_MEMBER_TABLE.MEMBERNAME)))
                .and(QUEUE_MEMBER_TABLE.PAUSED.notEqual(100))
                .and(PERSON_LIST.LICENSE_LIST.like("%" + LicenseListType.STAT.getCode() + "%"))
                .groupBy(QUEUE_MEMBER_TABLE.QUEUE_NAME, QUEUE_MEMBER_TABLE.PAUSED)
                .fetchInto(MemberStatusOfHunt.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> findAllByQueueName(List<QueueName> queueNameList, Boolean isGroupBy) {
        Condition condition = DSL.noCondition();

        if (isGroupBy != null)
            groupBy = isGroupBy;

        for (int i = 0; i < queueNameList.size(); i++) {
            if (i == 0)
                condition = condition.and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueNameList.get(i).getName()));
            else
                condition = condition.or(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueNameList.get(i).getName()));
        }
        return findAll(condition, Collections.singletonList(QUEUE_MEMBER_TABLE.MEMBERNAME.desc()));
    }

    public CenterMemberStatusCountEntity getCenterStatusCount() {
        return dsl.select(
                        count(when(QUEUE_MEMBER_TABLE.PAUSED.eq(0), 1)).as("waitCount"),
                        count(when(QUEUE_MEMBER_TABLE.PAUSED.eq(2), 1)).as("postProcessCount"),
                        count(when(QUEUE_MEMBER_TABLE.PAUSED.notEqual(0).and(QUEUE_MEMBER_TABLE.PAUSED.notEqual(1)).and(QUEUE_MEMBER_TABLE.PAUSED.notEqual(2)), 1)).as("etc_count"),
                        count(when(QUEUE_MEMBER_TABLE.PAUSED.ne(9), 1)).as("workingPerson"),
                        count(when(QUEUE_MEMBER_TABLE.IS_LOGIN.eq("Y"), 1)).as("loginCount"),
                        count(when(QUEUE_MEMBER_TABLE.IS_LOGIN.eq("N"), 1)).as("logoutCount")
                )
                .from(QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(EMPTY))
                .fetchOneInto(CenterMemberStatusCountEntity.class);
    }

    public List<QueueNameRepository.QueueMemberStatus> memberStatusesByPause() {
        return dsl.select(QUEUE_MEMBER_TABLE.PAUSED
                        , QUEUE_MEMBER_TABLE.IS_LOGIN
                        , DSL.count().as("cnt")
                )
                .from(QUEUE_MEMBER_TABLE)
                .where(compareCompanyId())
                .and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(EMPTY))
                .groupBy(QUEUE_MEMBER_TABLE.PAUSED, QUEUE_MEMBER_TABLE.IS_LOGIN)
                .fetchInto(QueueNameRepository.QueueMemberStatus.class);
    }

    public List<PDSQueuePersonResponse> getPDSQueuePersonStatus() {
        return dsl.select(PERSON_LIST.ID, QUEUE_MEMBER_TABLE.PAUSED, QUEUE_MEMBER_TABLE.QUEUE_NAME)
                .from(QUEUE_MEMBER_TABLE)
                .innerJoin(PERSON_LIST).on(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(PERSON_LIST.ID))
                .where(compareCompanyId())
                .groupBy(PERSON_LIST.ID)
                .fetchInto(PDSQueuePersonResponse.class);
    }

    public Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> findAllQueueMember() {
        return findAll(compareCompanyId().and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(EMPTY))).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable::getMembername, e -> e));
    }

    public Map<UInteger, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable> findAllQueueName(String userid) {
        return findAll(compareCompanyId().and(QUEUE_MEMBER_TABLE.QUEUE_NAME.like("QUEUE%")).and(QUEUE_MEMBER_TABLE.USERID.eq(userid)).and(QUEUE_MEMBER_TABLE.USERID.notEqual(""))).stream().collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable::getUniqueid, e -> e));
    }
}
