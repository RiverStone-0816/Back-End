package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsQueueName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueTable;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.QueueMemberTableRecord;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.CallDistributionStrategy;
import kr.co.eicn.ippbx.model.enums.PDSResultGroupStrategy;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.PDSResultGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PDSResultGroupPersonFormRequest;
import kr.co.eicn.ippbx.model.form.PDSResultGroupUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.PDSResultGroupSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.StringUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
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
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsQueueName.PDS_QUEUE_NAME;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Getter
@Repository
public class PDSQueueNameRepository extends EicnBaseRepository<PdsQueueName, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName, String> {
    protected final Logger logger = LoggerFactory.getLogger(PDSQueueNameRepository.class);

    private final QueueMemberTableRepository queueMemberTableRepository;
    private final QueueTableRepository queueTableRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;
    private final WebSecureHistoryRepository webSecureHistoryRepository;

    public PDSQueueNameRepository(QueueMemberTableRepository queueMemberTableRepository, QueueTableRepository queueTableRepository, PBXServerInterface pbxServerInterface, CacheService cacheService, WebSecureHistoryRepository webSecureHistoryRepository) {
        super(PDS_QUEUE_NAME, PDS_QUEUE_NAME.NAME, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName.class);
        this.queueTableRepository = queueTableRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.queueMemberTableRepository = queueMemberTableRepository;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;

        orderByFields.add(PDS_QUEUE_NAME.HAN_NAME.asc());
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName> pagination(PDSResultGroupSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(PDSResultGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }

    public String insertOnGeneratedKey(PDSResultGroupFormRequest form) {
        final PdsQueueName sequenceSeed = PDS_QUEUE_NAME.as("SEQUENCE_SEED");
        final Integer nextSequence = dsl.select(DSL.ifnull(DSL.max(sequenceSeed.SEQ), 0).add(1)).from(sequenceSeed).fetchOneInto(Integer.class);
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName queueNameRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName();
        final String queueName = "PDS_QUEUE".concat(String.valueOf(nextSequence));

        queueNameRecord.setSeq(nextSequence);
        queueNameRecord.setName(queueName);
        queueNameRecord.setHanName(form.getHanName());
        queueNameRecord.setStrategy(form.getStrategy());
        queueNameRecord.setBusyContext(form.getBusyContext());
        queueNameRecord.setHost(form.getRunHost());
        queueNameRecord.setCnt(0);
        queueNameRecord.setCompanyId(getCompanyId());

        final QueueTable queueTableRecord = new QueueTable();
        queueTableRecord.setName(queueName);
        queueTableRecord.setMusiconhold("default");
        queueTableRecord.setAnnounce(EMPTY);
        queueTableRecord.setContext(EMPTY);
        queueTableRecord.setMonitorType("1");
        queueTableRecord.setMonitorFormat("wav");
        queueTableRecord.setQueueYouarenext("ars/queue-youarenext");
        queueTableRecord.setQueueThereare("ars/queue-thereare");
        queueTableRecord.setQueueCallswaiting("ars/queue-callswaiting");
        queueTableRecord.setQueueHoldtime("ars/queue-holdtime");
        queueTableRecord.setQueueMinutes("ars/queue-minutes");
        queueTableRecord.setQueueSeconds("ars/queue-seconds");
        queueTableRecord.setQueueLessthan("ars/queue-lessthan");
        queueTableRecord.setQueueThankyou("ars/queue-thankyou");
        queueTableRecord.setQueueReporthold("ars/queue-reporthold");
        queueTableRecord.setAnnounceFrequency(0);
        queueTableRecord.setAnnounceRoundSeconds(0);
        queueTableRecord.setAnnounceHoldtime("no");
        queueTableRecord.setRetry(1);
        queueTableRecord.setWrapuptime(0);
        queueTableRecord.setMaxlen(20);
        queueTableRecord.setServicelevel(60);
        queueTableRecord.setStrategy(PDSResultGroupStrategy.SEQUENCE.getCode().equals(form.getStrategy())
                ? PDSResultGroupStrategy.RINGALL.getCode()
                : form.getStrategy());
        queueTableRecord.setJoinempty("yes");
        queueTableRecord.setLeavewhenempty("yes");//대기자가 있어도 기다리지않음
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

        insert(dsl, queueNameRecord);
        queueTableRepository.insert(dsl, queueTableRecord);

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(form.getRunHost()))
                .findFirst();

        Map<String, QueueMemberTable> huntOldMembers = queueMemberTableRepository.findAll(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                .stream()
                .collect(toMap(QueueMemberTable::getMembername, member -> member));

        if (optionalPbxServer.isPresent()) {
            try (DSLContext pbxDsl = pbxServerInterface.using(optionalPbxServer.get().getHost())) {
                insert(pbxDsl, queueNameRecord);
                queueTableRepository.insert(pbxDsl, queueTableRecord);
                huntOldMembers = queueMemberTableRepository.findAll(pbxDsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName))
                        .stream()
                        .collect(toMap(QueueMemberTable::getMembername, member -> member));
            }
        }

        final List<PDSResultGroupPersonFormRequest> addPersons = form.getAddPersons();
        int index = -1;
        for (PDSResultGroupPersonFormRequest addPerson : addPersons) {
            index = index + 1;
            final String userId = addPerson.getUserId();
            // 기존 사용자는 업데이트
            final QueueMemberTable oldMember = huntOldMembers.get(userId);
            if (oldMember != null) {
                final QueueMemberTableRecord record = new QueueMemberTableRecord();
                record.setQueueName(oldMember.getQueueName());
                record.setMembername(oldMember.getMembername());
                record.setPenalty(PDSResultGroupStrategy.SEQUENCE.getCode().equals(form.getStrategy()) ? index + 1 : 0);

                queueMemberTableRepository.updateQueueNameAndMemberName(dsl, record);

                optionalPbxServer.ifPresent(server -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(optionalPbxServer.get().getHost())) {
                        queueMemberTableRepository.updateQueueNameAndMemberName(pbxDsl, record);
                    }
                });
                huntOldMembers.remove(userId);
            } else {
                Optional<QueueMemberTable> optionalEmptyOrHuntQueueNameQueueMemberTable;
                if (optionalPbxServer.isPresent()) {
                    try (DSLContext pbxDsl = pbxServerInterface.using(form.getRunHost())) {
                        optionalEmptyOrHuntQueueNameQueueMemberTable = queueMemberTableRepository.findAll(pbxDsl, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(userId).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(QUEUE_MEMBER_TABLE.MEMBERNAME)))
                                .stream()
                                .findFirst();
                    }
                } else {
                    optionalEmptyOrHuntQueueNameQueueMemberTable = queueMemberTableRepository.findAll(dsl, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(userId).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.notEqual(QUEUE_MEMBER_TABLE.MEMBERNAME)))
                            .stream()
                            .findFirst();
                }
                if (optionalEmptyOrHuntQueueNameQueueMemberTable.isPresent()) {
                    QueueMemberTable insertQueueMemberTableRecord = optionalEmptyOrHuntQueueNameQueueMemberTable.get();
                    insertQueueMemberTableRecord.setPenalty(PDSResultGroupStrategy.SEQUENCE.getCode().equals(form.getStrategy()) ? index + 1 : 0);
                    insertQueueMemberTableRecord.setQueueName(queueName);
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
                        optionalDBRoutingQueueMemberTable = queueMemberTableRepository.findAll(dsl, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(userId))
                                .stream()
                                .findFirst();
                    } else {
                        try (DSLContext pbx = pbxServerInterface.using(form.getRunHost())) {
                            optionalDBRoutingQueueMemberTable = queueMemberTableRepository.findAll(pbx, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(userId))
                                    .stream()
                                    .findFirst();
                        }
                    }
                    final QueueMemberTable insertQueueMemberTableRecord = new QueueMemberTable();
                    insertQueueMemberTableRecord.setQueueName(queueName);
                    insertQueueMemberTableRecord.setMembername(userId);
                    insertQueueMemberTableRecord.setInterface("SIP/".concat(userId));
                    insertQueueMemberTableRecord.setPenalty(PDSResultGroupStrategy.SEQUENCE.getCode().equals(form.getStrategy()) ? index + 1 : 0);
                    if (optionalDBRoutingQueueMemberTable.isPresent()) {
                        final QueueMemberTable r = optionalDBRoutingQueueMemberTable.get();
                        insertQueueMemberTableRecord.setIsLogin(r.getIsLogin());
                        insertQueueMemberTableRecord.setPaused(r.getPaused());
                    } else {
                        insertQueueMemberTableRecord.setIsLogin("N");
                        insertQueueMemberTableRecord.setPaused(3);
                    }
                    insertQueueMemberTableRecord.setCompanyId(getCompanyId());
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
            actionData += "," + form.getAddPersons().stream().map(PDSResultGroupPersonFormRequest::getUserId).collect(joining("|"));

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_GROUP, WebSecureActionSubType.ADD, StringUtils.subStringBytes(actionData, 400));

        return queueName;
    }

    public void update(PDSResultGroupUpdateFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName modQueueTable = findOneIfNullThrow(form.getName());

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(form.getRunHost()))
                .findFirst();

        if (modQueueTable != null) {
            final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName modQueueName = modelMapper.map(modQueueTable, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName.class);
            modQueueName.setHanName(form.getHanName());
            modQueueName.setStrategy(form.getStrategy());
            modQueueName.setBusyContext(form.getBusyContext());
            modQueueName.setHost(form.getRunHost());
            modQueueName.setCompanyId(getCompanyId());


            modQueueTable.setStrategy(CallDistributionStrategy.RINGALL.getCode().equals(form.getStrategy())
                    ? CallDistributionStrategy.RINGALL.getCode() : form.getStrategy());


            updateByKey(modQueueName, modQueueTable.getName());
            queueTableRepository.updateByKey(modQueueTable, modQueueTable.getName());

            optionalPbxServer.ifPresent(server -> {
                try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                    updateByKey(pbxDsl, modQueueName, modQueueTable.getName());
                    queueTableRepository.updateByKey(pbxDsl, modQueueName, modQueueTable.getName());
                }
            });
        }

        queueMemberTableRepository.delete(dsl, compareCompanyId(QUEUE_MEMBER_TABLE).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(modQueueTable.getName())));
        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                queueMemberTableRepository.delete(pbx, compareCompanyId(QUEUE_MEMBER_TABLE).and(QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(modQueueTable.getName())));
            }
        });

        if (form.getAddPersons() != null) {
            int index = -1;
            for (PDSResultGroupPersonFormRequest addPerson : form.getAddPersons()) {
                index = index + 1;
                final String peer = addPerson.getUserId();
                Optional<QueueMemberTable> optionalEmptyOrHuntQueueNameQueueMemberTable;
                if (optionalPbxServer.isPresent()) {
                    try (DSLContext pbxDsl = pbxServerInterface.using(form.getRunHost())) {
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
                    final QueueMemberTable insertRecord = optionalEmptyOrHuntQueueNameQueueMemberTable.get();
                    insertRecord.setPenalty(PDSResultGroupStrategy.SEQUENCE.getCode().equals(form.getStrategy()) ? index + 1 : 0);
                    insertRecord.setQueueName(modQueueTable.getName());
                    insertRecord.setCompanyId(getCompanyId());
                    insertRecord.setUniqueid(null);
                    queueMemberTableRepository.insert(dsl, dsl.newRecord(QUEUE_MEMBER_TABLE, insertRecord));

                    optionalPbxServer.ifPresent(server -> {
                        try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                            queueMemberTableRepository.insert(pbx, dsl.newRecord(QUEUE_MEMBER_TABLE, insertRecord));
                        }
                    });
                } else {

                    final Optional<QueueMemberTable> optionalDBRoutingQueueMemberTable;
                    if (!optionalPbxServer.isPresent()) {
                        optionalDBRoutingQueueMemberTable = queueMemberTableRepository.findAll(dsl, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(peer))
                                .stream()
                                .findFirst();
                    } else {
                        try (DSLContext pbx = pbxServerInterface.using(form.getRunHost())) {
                            optionalDBRoutingQueueMemberTable = queueMemberTableRepository.findAll(pbx, QUEUE_MEMBER_TABLE.MEMBERNAME.eq(peer))
                                    .stream()
                                    .findFirst();
                        }
                    }

                    final QueueMemberTable insertRecord = new QueueMemberTable();
                    insertRecord.setQueueName(modQueueTable.getName());
                    insertRecord.setMembername(peer);
                    insertRecord.setInterface("SIP/".concat(peer));
                    insertRecord.setPenalty(PDSResultGroupStrategy.SEQUENCE.getCode().equals(form.getStrategy()) ? index + 1 : 0);

                    if (optionalDBRoutingQueueMemberTable.isPresent()) {
                        final QueueMemberTable r = optionalDBRoutingQueueMemberTable.get();
                        insertRecord.setIsLogin(r.getIsLogin());
                    } else {
                        insertRecord.setIsLogin("N");
                    }
                    insertRecord.setPaused(0);
                    insertRecord.setCompanyId(getCompanyId());
                    insertRecord.setUniqueid(null);
                    queueMemberTableRepository.insert(dsl, dsl.newRecord(QUEUE_MEMBER_TABLE, insertRecord));

                    optionalPbxServer.ifPresent(server -> {
                        try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                            queueMemberTableRepository.insert(pbx, dsl.newRecord(QUEUE_MEMBER_TABLE, insertRecord));
                        }
                    });
                }
            }
        }

        String actionData = form.getHanName();
        if (form.getAddPersons() != null)
            actionData += "," + form.getAddPersons().stream().map(PDSResultGroupPersonFormRequest::getUserId).collect(joining("|"));

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_GROUP, WebSecureActionSubType.MOD, StringUtils.subStringBytes(actionData, 400));
    }

    public int delete(String queueName) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsQueueName pdsQueueName = findOneIfNullThrow(queueName);
        // queue_table 삭제, queue_member_table 삭제
        queueTableRepository.delete(dsl, queueName);
        queueMemberTableRepository.delete(dsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName));

        final Optional<CompanyServerEntity> optionalPbxServer = cacheService.pbxServerList(getCompanyId())
                .stream()
                .filter(e -> e.getHost().equals(pdsQueueName.getHost()))
                .findFirst();

        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(server.getHost())) {
                queueTableRepository.delete(pbxDsl, queueName);
                queueMemberTableRepository.delete(pbxDsl, QUEUE_MEMBER_TABLE.QUEUE_NAME.eq(queueName));
            }
        });
        // queue_name 삭제
        final int r = super.delete(dsl, queueName);

        optionalPbxServer.ifPresent(server -> {
            try (DSLContext pbx = pbxServerInterface.using(server.getHost())) {
                super.delete(pbx, queueName);
            }
        });

        webSecureHistoryRepository.insert(WebSecureActionType.PDS_GROUP, WebSecureActionSubType.DEL, pdsQueueName.getHanName() + "(" + queueName + ")");

        return r;
    }

}
