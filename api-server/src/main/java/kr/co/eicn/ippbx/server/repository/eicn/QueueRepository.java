package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.DashboardInfoDashboardType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueTable;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.QueueMemberTableRecord;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.QueueEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.QueueFormRequest;
import kr.co.eicn.ippbx.model.form.QueueFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.QueuePersonFormRequest;
import kr.co.eicn.ippbx.model.form.QueueUpdateBlendingFormRequest;
import kr.co.eicn.ippbx.model.search.QueueSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.StringUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.QUEUE_NAME;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueTable.QUEUE_TABLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Getter
@Repository
public class QueueRepository extends EicnBaseRepository<QueueName, QueueEntity, String> {
    protected final Logger logger = LoggerFactory.getLogger(QueueRepository.class);

    private final Number070Repository numberRepository;
    private final QueueNameRepository queueNameRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final QueueTableRepository queueTableRepository;
    private final CompanyTreeRepository companyTreeRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;
    private final DashboardInfoRepository dashboardInfoRepository;
    private final CurrentEICNCdrRepository currentEicnCdrRepository;
    private final PersonListRepository personListRepository;

    public QueueRepository(Number070Repository numberRepository, QueueNameRepository queueNameRepository, QueueMemberTableRepository queueMemberTableRepository, QueueTableRepository queueTableRepository,
                           CompanyTreeRepository companyTreeRepository, WebSecureHistoryRepository webSecureHistoryRepository,
                           PBXServerInterface pbxServerInterface, CacheService cacheService, DashboardInfoRepository dashboardInfoRepository,
                           CurrentEICNCdrRepository currentEicnCdrRepository, PersonListRepository personListRepository) {
        super(QUEUE_NAME, QUEUE_NAME.NAME, QueueEntity.class);
        this.personListRepository = personListRepository;

        orderByFields.add(QUEUE_NAME.HAN_NAME.asc());

        this.numberRepository = numberRepository;
        this.queueNameRepository = queueNameRepository;
        this.queueMemberTableRepository = queueMemberTableRepository;
        this.queueTableRepository = queueTableRepository;
        this.companyTreeRepository = companyTreeRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
        this.dashboardInfoRepository = dashboardInfoRepository;
        this.currentEicnCdrRepository = currentEicnCdrRepository;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(QUEUE_TABLE).on(QUEUE_TABLE.NAME.eq(QUEUE_NAME.NAME))
                .where();
    }

    @Override
    protected RecordMapper<Record, QueueEntity> getMapper() {
        return record -> {
            final QueueEntity entity = record.into(QUEUE_NAME).into(QueueEntity.class);
            entity.setQueueTable(record.into(QUEUE_TABLE).into(QueueTable.class));
            entity.setSubGroupQueueName(findOne(record.get(QUEUE_NAME.NO_CONNECT_DATA)));
            return entity;
        };
    }

    public Pagination<QueueEntity> pagination(QueueSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public String insertOnGeneratedKey(QueueFormRequest form) {
        final Number_070 number = numberRepository.findOneIfNullThrow(form.getNumber());
        final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());

        queueTableRepository.existsKeyIfNonNullThrow(form.getHanName(), "헌트명");

        final QueueName sequenceSeed = QUEUE_NAME.as("SEQUENCE_SEED");
        final Integer nextSequence = dsl.select(DSL.ifnull(DSL.max(sequenceSeed.SEQ), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName queueNameRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName();
        final String queueName = "QUEUE".concat(String.valueOf(nextSequence));

        queueNameRecord.setSeq(nextSequence);
        queueNameRecord.setName(queueName);
        queueNameRecord.setHanName(form.getHanName());
        queueNameRecord.setNumber(form.getNumber());
        queueNameRecord.setQueueTimeout(form.getQueueTimeout());
        queueNameRecord.setStrategy(form.getStrategy());
        queueNameRecord.setCnt(0);
        queueNameRecord.setRetryMaxCnt(form.getIsRetry() ? form.getRetryMaxCount() : 0);
        queueNameRecord.setRetrySound(form.getIsRetry() ? (form.getRetrySoundCode().equals("TTS") ? form.getRetrySoundCode() + "|" + form.getTtsData() : form.getRetrySoundCode()) : "");
        queueNameRecord.setRetryAction(form.getIsRetry() ? form.getRetryAction() : "");
        queueNameRecord.setNoConnectKind(form.getNoConnectKind().getCode());
        queueNameRecord.setNoConnectData(form.getNoConnectData());
        queueNameRecord.setSvcNumber(form.getSvcNumber());
        queueNameRecord.setHost(number.getHost());
        queueNameRecord.setCompanyId(getCompanyId());
        queueNameRecord.setIsForwarding(defaultString(form.getIsForwarding(), "N"));
        queueNameRecord.setHuntForwarding(form.getHuntForwarding());

        final QueueTable queueTableRecord = new QueueTable();
        queueTableRecord.setName(queueName);
        queueTableRecord.setMusiconhold(defaultString(form.getMusiconhold()));
        queueTableRecord.setAnnounce(EMPTY);
        queueTableRecord.setContext(EMPTY);
        queueTableRecord.setTimeout(CallDistributionStrategy.RINGALL.getCode().equals(form.getStrategy())
                ? form.getQueueTimeout() == null ? 30 : form.getQueueTimeout()
                : form.getTimeout() == null ? 15 : form.getTimeout());
        queueTableRecord.setLeavewhenempty("no");
        queueTableRecord.setMonitorType("1");
        queueTableRecord.setAnnounceFrequency(0);
        queueTableRecord.setAnnounceRoundSeconds(0);
        queueTableRecord.setAnnounceHoldtime("no");
        queueTableRecord.setRetry(1);
        queueTableRecord.setWrapuptime(0);
        queueTableRecord.setServicelevel(60);
        queueTableRecord.setMaxlen(form.getMaxlen());
        queueTableRecord.setStrategy(CallDistributionStrategy.RINGALL.getCode().equals(form.getStrategy())
                ? CallDistributionStrategy.RINGALL.getCode()
                : CallDistributionStrategy.SKILL.getCode().equals(form.getStrategy()) || CallDistributionStrategy.CALLRATE.getCode().equals(form.getStrategy())
                ? CallDistributionStrategy.RRMEMORY.getCode()
                : form.getStrategy());
        queueTableRecord.setJoinempty("yes");
        queueTableRecord.setLeavewhenempty("no");   // 대기자가 있어도 기다림
        queueTableRecord.setEventmemberstatus(false);
        queueTableRecord.setEventwhencalled(true);
        queueTableRecord.setReportholdtime(false);
        queueTableRecord.setMemberdelay(0);
        queueTableRecord.setWeight(0);
        queueTableRecord.setTimeoutrestart(false);
        queueTableRecord.setPeriodicAnnounce(EMPTY);
        queueTableRecord.setPeriodicAnnounceFrequency(0);
        queueTableRecord.setRinginuse(false);
        queueTableRecord.setSetinterfacevar(true);

        number.setStatus((byte) 1);
        number.setType((byte) 0);

        if (companyTree != null) {
            queueNameRecord.setGroupCode(companyTree.getGroupCode());
            queueNameRecord.setGroupTreeName(companyTree.getGroupTreeName());
            queueNameRecord.setGroupLevel(companyTree.getGroupLevel());
        }

        queueNameRepository.insert(dsl, queueNameRecord);
        queueTableRepository.insert(dsl, queueTableRecord);
        numberRepository.updateByKey(dsl, number, number.getNumber());

        dashboardInfoRepository.insert(form.getHanName().concat(" 모니터링"), DashboardInfoDashboardType.hunt_monitor, form.getNumber(), 4);

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(number.getHost()))
                .findFirst();

        Map<String, QueueMemberTable> huntOldMembers = queueMemberTableRepository.findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                .stream()
                .collect(toMap(QueueMemberTable::getMembername, member -> member));

        if (optionalPbxServer.isPresent()) {
            try (DSLContext pbxDsl = pbxServerInterface.using(optionalPbxServer.get().getHost())) {
                queueNameRepository.insert(pbxDsl, queueNameRecord);
                queueTableRepository.insert(pbxDsl, queueTableRecord);
                numberRepository.updateByKey(pbxDsl, number, number.getNumber());
                huntOldMembers = queueMemberTableRepository.findAll(pbxDsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                        .stream()
                        .collect(toMap(QueueMemberTable::getMembername, member -> member));
            }
        }

        final List<QueuePersonFormRequest> addPersons = form.getAddPersons();
        for (QueuePersonFormRequest addPerson : addPersons) {
            final String peer = addPerson.getPeer();
            // 기존 사용자는 업데이트
            final String userId = personListRepository.findIdByPeer(peer);
            final QueueMemberTable oldMember = huntOldMembers.get(peer);
            if (oldMember != null) {
                final QueueMemberTableRecord record = new QueueMemberTableRecord();
                record.setQueueName(oldMember.getQueueName());
                record.setMembername(oldMember.getMembername());
                record.setUserid(userId);
                record.setPenalty(CallDistributionStrategy.SKILL.getCode().equals(form.getStrategy()) ? addPerson.getPenalty() : 0);
                record.setCallRate(CallDistributionStrategy.CALLRATE.getCode().equals(form.getStrategy()) ? addPerson.getCallRate() : 0);

                queueMemberTableRepository.updateQueueNameAndMemberName(dsl, record);

                optionalPbxServer.ifPresent(server -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(optionalPbxServer.get().getHost())) {
                        queueMemberTableRepository.updateQueueNameAndMemberName(pbxDsl, record);
                    }
                });
                huntOldMembers.remove(peer);
            } else {
                Optional<QueueMemberTable> optionalEmptyOrHuntQueueNameQueueMemberTable;
                if (optionalPbxServer.isPresent()) {
                    try (DSLContext pbxDsl = pbxServerInterface.using(number.getHost())) {
                        optionalEmptyOrHuntQueueNameQueueMemberTable = queueMemberTableRepository.findAll(pbxDsl, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(peer).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(QUEUE_MEMBER_TABLE.MEMBERNAME)))
                                .stream()
                                .findFirst();
                    }
                } else {
                    optionalEmptyOrHuntQueueNameQueueMemberTable = queueMemberTableRepository.findAll(dsl, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(peer).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(QUEUE_MEMBER_TABLE.MEMBERNAME)))
                            .stream()
                            .findFirst();
                }
                if (optionalEmptyOrHuntQueueNameQueueMemberTable.isPresent()) {
                    QueueMemberTable insertQueueMemberTableRecord = optionalEmptyOrHuntQueueNameQueueMemberTable.get();
                    insertQueueMemberTableRecord.setPenalty(CallDistributionStrategy.SKILL.getCode().equals(form.getStrategy()) ? addPerson.getPenalty() : 0);
                    insertQueueMemberTableRecord.setCallRate(CallDistributionStrategy.CALLRATE.getCode().equals(form.getStrategy()) ? addPerson.getCallRate() : 0);
                    insertQueueMemberTableRecord.setQueueName(queueName);
                    insertQueueMemberTableRecord.setQueueNumber(number.getNumber());
                    insertQueueMemberTableRecord.setUserid(userId);
                    insertQueueMemberTableRecord.setUniqueid(null);
                    queueMemberTableRepository.insert(dsl, insertQueueMemberTableRecord);

                    optionalPbxServer.ifPresent(server -> {
                        try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                            queueMemberTableRepository.insert(pbxDsl, insertQueueMemberTableRecord);
                        }
                    });
                } else {
                    final Optional<QueueMemberTable> optionalDBRoutingQueueMemberTable;
                    if (!optionalPbxServer.isPresent()) {
                        optionalDBRoutingQueueMemberTable = queueMemberTableRepository.findByOneQueueMemberTableQueueNameAndMemberName(dsl, peer);
                    } else {
                        try (DSLContext pbx = pbxServerInterface.using(number.getHost())) {
                            optionalDBRoutingQueueMemberTable = queueMemberTableRepository.findByOneQueueMemberTableQueueNameAndMemberName(pbx, peer);
                        }
                    }
                    final QueueMemberTable insertQueueMemberTableRecord = new QueueMemberTable();
                    insertQueueMemberTableRecord.setPenalty(CallDistributionStrategy.SKILL.getCode().equals(form.getStrategy()) ? addPerson.getPenalty() : 0);
                    insertQueueMemberTableRecord.setCallRate(CallDistributionStrategy.CALLRATE.getCode().equals(form.getStrategy()) ? addPerson.getCallRate() : 0);
                    insertQueueMemberTableRecord.setQueueName(queueName);
                    insertQueueMemberTableRecord.setQueueNumber(number.getNumber());
                    insertQueueMemberTableRecord.setMembername(peer);
                    insertQueueMemberTableRecord.setInterface("SIP/".concat(peer));
                    insertQueueMemberTableRecord.setUserid(userId);
                    insertQueueMemberTableRecord.setPaused(0);
                    insertQueueMemberTableRecord.setCompanyId(getCompanyId());

                    if (optionalDBRoutingQueueMemberTable.isPresent()) {
                        final QueueMemberTable r = optionalDBRoutingQueueMemberTable.get();
                        insertQueueMemberTableRecord.setIsLogin(r.getIsLogin());
                    } else {
                        insertQueueMemberTableRecord.setIsLogin("N");
                    }
                    queueMemberTableRepository.insert(dsl, insertQueueMemberTableRecord);

                    optionalPbxServer.ifPresent(server -> {
                        try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                            queueMemberTableRepository.insert(pbxDsl, insertQueueMemberTableRecord);
                        }
                    });
                }
            }
        }

        huntOldMembers.forEach((memberName, member) -> {
            queueMemberTableRepository.deleteByQueueNameAndMemberName(dsl, member.getQueueName(), memberName);
            optionalPbxServer.ifPresent(server -> {
                try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                    queueMemberTableRepository.deleteByQueueNameAndMemberName(pbx, member.getQueueName(), memberName);
                }
            });
        });

        String actionData = form.getHanName();
        if (form.getAddPersons() != null)
            actionData += "," + form.getAddPersons().stream().map(QueuePersonFormRequest::getPeer).collect(joining("|"));

        webSecureHistoryRepository.insert(WebSecureActionType.QUEUE, WebSecureActionSubType.ADD, StringUtils.subStringBytes(actionData, 400));

        return queueName;
    }

    public void update(QueueFormUpdateRequest form) {
        final QueueEntity modQueueEntity = findOneIfNullThrow(form.getName());
        final Number_070 number = numberRepository.findOneIfNullThrow(form.getNumber());
        final Number_070 originalNumber = numberRepository.findOneIfNullThrow(modQueueEntity.getNumber());
        final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(number.getHost()))
                .findFirst();

        final QueueTable modQueueTable = modQueueEntity.getQueueTable();

        if (modQueueTable != null) {
            final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName modQueueName = modelMapper.map(modQueueEntity, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName.class);
            modQueueName.setHanName(form.getHanName());
            modQueueName.setNumber(form.getNumber());
            modQueueName.setSvcNumber(form.getSvcNumber());
            modQueueName.setStrategy(form.getStrategy());
            modQueueName.setQueueTimeout(form.getQueueTimeout());
            modQueueName.setRetryMaxCnt(form.getIsRetry() ? form.getRetryMaxCount() : 0);
            modQueueName.setRetrySound(form.getIsRetry() ? (form.getRetrySoundCode().equals("TTS") ? form.getRetrySoundCode() + "|" + form.getTtsData() : form.getRetrySoundCode()) : "");
            modQueueName.setRetryAction(form.getIsRetry() ? form.getRetryAction() : "");
            modQueueName.setNoConnectKind(form.getNoConnectKind().getCode());
            modQueueName.setNoConnectData(form.getNoConnectData());
            modQueueName.setIsForwarding(defaultString(form.getIsForwarding(), "N"));
            modQueueName.setHuntForwarding(form.getHuntForwarding());

            modQueueTable.setStrategy(CallDistributionStrategy.RINGALL.getCode().equals(form.getStrategy())
                    ? CallDistributionStrategy.RINGALL.getCode()
                    : CallDistributionStrategy.SKILL.getCode().equals(form.getStrategy()) || CallDistributionStrategy.CALLRATE.getCode().equals(form.getStrategy())
                    ? CallDistributionStrategy.RRMEMORY.getCode()
                    : form.getStrategy());
            modQueueTable.setMaxlen(form.getMaxlen());
            modQueueTable.setMusiconhold(defaultString(form.getMusiconhold()));
            modQueueTable.setRinginuse(false);
            modQueueTable.setTimeout(CallDistributionStrategy.RINGALL.getCode().equals(form.getStrategy())
                    ? form.getQueueTimeout() == null ? 30 : form.getQueueTimeout()
                    : form.getTimeout() == null ? 15 : form.getTimeout());
            modQueueTable.setAnnounceFrequency(0);
            modQueueTable.setAnnounceRoundSeconds(0);
            modQueueTable.setAnnounceHoldtime("no");

            if (companyTree != null) {
                modQueueName.setGroupCode(companyTree.getGroupCode());
                modQueueName.setGroupTreeName(companyTree.getGroupTreeName());
                modQueueName.setGroupLevel(companyTree.getGroupLevel());
            }

            queueNameRepository.updateByKey(modQueueName, modQueueEntity.getName());
            queueTableRepository.updateByKey(modQueueTable, modQueueEntity.getName());

            dashboardInfoRepository.updateByValue(form.getHanName().concat(" 모니터링"), form.getNumber(), modQueueEntity.getNumber());

            if (!number.getNumber().equals(originalNumber.getNumber())) {
                number.setStatus(Number070Status.USE.getCode());
                number.setType(NumberType.HUNT.getCode());
                // Number_070 상태 업데이트
                numberRepository.updateByKey(number, number.getNumber());
                numberRepository.updateStatusAllPbxServers(originalNumber.getNumber(), Number070Status.NON_USE.getCode());
            }

            optionalPbxServer.ifPresent(server -> {
                try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                    queueNameRepository.updateByKey(pbxDsl, modQueueName, modQueueEntity.getName());
                    queueTableRepository.updateByKey(pbxDsl, modQueueTable, modQueueEntity.getName());
                    // Number_070 상태 업데이트
                    numberRepository.updateByKey(pbxDsl, number, number.getNumber());
                }
            });
        }

        queueMemberTableRepository.delete(dsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(modQueueEntity.getName()));

        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                queueMemberTableRepository.delete(pbxDsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(modQueueEntity.getName()));
            }
        });

        if (form.getAddPersons() != null) {
            for (QueuePersonFormRequest addPerson : form.getAddPersons()) {
                final String peer = addPerson.getPeer();
                final String userId = personListRepository.findIdByPeer(peer);
                Optional<QueueMemberTable> optionalPrivateQueueNameQueueMemberTableRecord;
                if (!optionalPbxServer.isPresent()) {
                    optionalPrivateQueueNameQueueMemberTableRecord = queueMemberTableRepository.findByOneQueueMemberTableQueueNameAndMemberName(dsl, peer);
                } else {
                    try (DSLContext pbx = pbxServerInterface.using(number.getHost())) {
                        optionalPrivateQueueNameQueueMemberTableRecord = queueMemberTableRepository.findByOneQueueMemberTableQueueNameAndMemberName(pbx, peer);
                    }
                }
                if (optionalPrivateQueueNameQueueMemberTableRecord.isPresent()) {
                    final QueueMemberTable insertRecord = optionalPrivateQueueNameQueueMemberTableRecord.get();
                    insertRecord.setPenalty(CallDistributionStrategy.SKILL.getCode().equals(form.getStrategy()) ? addPerson.getPenalty() : 0);
                    insertRecord.setCallRate(CallDistributionStrategy.CALLRATE.getCode().equals(form.getStrategy()) ? addPerson.getCallRate() : 0);
                    insertRecord.setQueueName(modQueueEntity.getName());
                    insertRecord.setQueueNumber(number.getNumber());
                    insertRecord.setUserid(userId);
                    insertRecord.setBlendingMode("N");
                    insertRecord.setUniqueid(null);

                    queueMemberTableRepository.insert(dsl, insertRecord);

                    optionalPbxServer.ifPresent(server -> {
                        try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                            queueMemberTableRepository.insert(pbx, insertRecord);
                        }
                    });
                }
            }
        }

        String actionData = form.getHanName();
        if (form.getAddPersons() != null)
            actionData += "," + form.getAddPersons().stream().map(QueuePersonFormRequest::getPeer).collect(joining("|"));

        webSecureHistoryRepository.insert(WebSecureActionType.QUEUE, WebSecureActionSubType.MOD, StringUtils.subStringBytes(actionData, 400));
    }

    public int delete(String queueName) {
        final QueueEntity queueEntity = findOneIfNullThrow(queueName);
        // queue_table 삭제, queue_member_table 삭제
        queueTableRepository.delete(dsl, queueName);
        queueMemberTableRepository.delete(dsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName));

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(queueEntity.getHost()))
                .findFirst();

        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                queueTableRepository.delete(pbxDsl, queueName);
                queueMemberTableRepository.delete(pbxDsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName));
            }
        });
        // 070내선전화번호 상태 업데이트
        numberRepository.updateStatus(dsl, queueEntity.getNumber(), (byte) 0);
        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                numberRepository.updateStatus(pbxDsl, queueEntity.getNumber(), (byte) 0);
            }
        });
        // 참조 예비헌트명 초기화
        dsl.update(QUEUE_NAME)
                .set(QUEUE_NAME.NO_CONNECT_DATA, EMPTY)
                .where(compareCompanyId())
                .and(QUEUE_NAME.NO_CONNECT_KIND.eq(NoConnectKind.HUNT.getCode()))
                .and(QUEUE_NAME.NO_CONNECT_DATA.eq(queueName))
                .execute();
        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                pbx.update(QUEUE_NAME)
                        .set(QUEUE_NAME.NO_CONNECT_DATA, EMPTY)
                        .where(compareCompanyId())
                        .and(QUEUE_NAME.NO_CONNECT_KIND.eq(NoConnectKind.HUNT.getCode()))
                        .and(QUEUE_NAME.NO_CONNECT_DATA.eq(queueName))
                        .execute();
            }
        });
        // queue_name 삭제
        final int r = super.delete(dsl, queueName);

        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                super.delete(pbx, queueName);
            }
        });

        dashboardInfoRepository.deleteByValue(queueEntity.getNumber());

        webSecureHistoryRepository.insert(WebSecureActionType.QUEUE, WebSecureActionSubType.DEL, queueEntity.getHanName() + "(" + queueName + ")");

        return r;
    }

    private List<Condition> conditions(QueueSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }

    public void blendingUpdate(QueueUpdateBlendingFormRequest form, String name) {
        dsl.update(QUEUE_NAME)
                .set(QUEUE_NAME.BLENDING_MODE, form.getBlendingMode())
                .set(QUEUE_NAME.BLENDING_WAIT_CNT, form.getBlendingMode().equals("W") ? form.getWaitingCount() : 0)
                .set(QUEUE_NAME.BLENDING_WAIT_KEEPTIME, form.getBlendingMode().equals("W") ? form.getWaitingKeepTime() : 0)
                .set(QUEUE_NAME.BLENDING_TIME_FROMTIME, form.getBlendingMode().equals("T") ? form.getStartHour() * 60 + form.getStartMinute() : 0)
                .set(QUEUE_NAME.BLENDING_TIME_TOTIME, form.getBlendingMode().equals("T") ? form.getEndHour() * 60 + form.getEndMinute() : 0)
                .where(QUEUE_NAME.NAME.eq(name))
                .execute();
    }

    public void blending(List<String> persons, String queueName) {
        QueueEntity queue = findOneIfNullThrow(queueName);
        List<QueueMemberTable> personList = queueMemberTableRepository.findAll();

        queueMemberTableRepository.deleteByQueueNameBlending(dsl, queueName);

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(queue.getHost()))
                .findFirst();

        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(optionalPbxServer.get().getHost())) {
                queueMemberTableRepository.deleteByQueueNameBlending(pbxDsl, queueName);
            }
        });

        for (String person : persons) {
            final String userId = personListRepository.findIdByPeer(person);
            QueueMemberTable queueMemberRecord = personList.stream().filter(e -> e.getQueueName().equals("QUEUE" + person) && e.getMembername().equals(person)).findFirst().orElse(null);

            if (queueMemberRecord != null) {
                queueMemberRecord.setUniqueid(null);
                queueMemberRecord.setQueueName(queueName);
                queueMemberRecord.setQueueNumber(queue.getNumber());
                queueMemberRecord.setBlendingMode(queue.getBlendingMode());
                queueMemberRecord.setPaused(100);
                queueMemberRecord.setUserid(userId);
                queueMemberRecord.setCompanyId(getCompanyId());
                queueMemberTableRepository.insert(queueMemberRecord);

                optionalPbxServer.ifPresent(server -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(optionalPbxServer.get().getHost())) {
                        queueMemberTableRepository.insert(pbxDsl, queueMemberRecord);
                    }
                });
            }
        }

        webSecureHistoryRepository.insert(WebSecureActionType.QUEUE, WebSecureActionSubType.MOD, queue.getHanName() + "(" + queueName + ")");
    }
}
