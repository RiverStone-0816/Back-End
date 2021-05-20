package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CallbackDist;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.CallbackDistRecord;
import kr.co.eicn.ippbx.model.entity.eicn.CallbackDistEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CallbackEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.CallbackHuntDistFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackRedistFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackUserDistFormRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.CALLBACK_DIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.CALLBACK_LIST;
import static kr.co.eicn.ippbx.util.StringUtils.subStringBytes;

@Getter
@Repository
public class CallbackDistRepository extends EicnBaseRepository<CallbackDist, CallbackDistEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CallbackDistRepository.class);

    private final CallbackRepository callbackRepository;
    private final ServiceRepository serviceRepository;
    private final QueueNameRepository queueNameRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final PersonListRepository personListRepository;
    private final TodoListRepository todoListRepository;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;

    public CallbackDistRepository(CallbackRepository callbackRepository, ServiceRepository serviceRepository, QueueNameRepository queueNameRepository, WebSecureHistoryRepository webSecureHistoryRepository, PersonListRepository personListRepository, TodoListRepository todoListRepository, PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(CALLBACK_DIST, CALLBACK_DIST.SEQ, CallbackDistEntity.class);
        this.callbackRepository = callbackRepository;
        this.serviceRepository = serviceRepository;
        this.queueNameRepository = queueNameRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.personListRepository = personListRepository;
        this.todoListRepository = todoListRepository;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    public void huntDistribution(CallbackHuntDistFormRequest form, String svcNumber) {
        CallbackDistRecord insertRecord = new CallbackDistRecord();
        insertRecord.setServiceNumber(svcNumber);
        insertRecord.setIvrkey("");
        insertRecord.setIvrkeyName("");
        insertRecord.setUserid("");
        insertRecord.setCompanyId(getCompanyId());

        // 고객사 PBX HOST정보
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        huntDistribution(dsl, insertRecord, form.getHunts());
        pbxServerList.forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                huntDistribution(pbxDsl, insertRecord, form.getHunts());
            }
        });

        webSecureHistoryRepository.insert(WebSecureActionType.CALLBACK, WebSecureActionSubType.DIST, form.getHunts().stream().reduce("", (a, b) -> a + " " + b));
    }

    public void huntDistribution(DSLContext dslContext, CallbackDistRecord record, List<String> huntNumberList) {
        dslContext.deleteFrom(CALLBACK_DIST)
                .where(compareCompanyId())
                .and(CALLBACK_DIST.SERVICE_NUMBER.eq(record.getServiceNumber()))
                .and(CALLBACK_DIST.HUNT_NUMBER.notIn(huntNumberList))
                .execute();

        final List<CallbackDistEntity> callbackDistList = dslContext.select(CALLBACK_DIST.fields())
                .from(CALLBACK_DIST).fetchInto(CallbackDistEntity.class);

        for (String huntNumber : huntNumberList) {
            if (callbackDistList.stream().anyMatch(e -> e.getHuntNumber().equals(huntNumber)))
                continue;
            record.setHuntNumber(huntNumber);
            dslContext.insertInto(CALLBACK_DIST)
                    .set(record)
                    .execute();
        }
    }

    public void userDistribution(CallbackUserDistFormRequest form, String svcNumber, String huntNumber) {
        CallbackDistRecord insertRecord = new CallbackDistRecord();
        insertRecord.setServiceNumber(svcNumber);
        insertRecord.setHuntNumber(huntNumber);
        insertRecord.setIvrkey("");
        insertRecord.setIvrkeyName("");
        insertRecord.setCompanyId(getCompanyId());

        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        userDistribution(dsl, insertRecord, form.getUsers());
        pbxServerList.forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                userDistribution(pbxDsl, insertRecord, form.getUsers());
            }
        });

        webSecureHistoryRepository.insert(WebSecureActionType.CALLBACK, WebSecureActionSubType.DIST, subStringBytes(String.join("|", form.getUsers()), 400));
    }

    public void userDistribution(DSLContext dslContext, CallbackDistRecord record, List<String> personList) {
        dslContext.deleteFrom(CALLBACK_DIST)
                .where(compareCompanyId())
                .and(CALLBACK_DIST.SERVICE_NUMBER.eq(record.getServiceNumber()))
                .and(CALLBACK_DIST.HUNT_NUMBER.eq(record.getHuntNumber()))
                .execute();

        for (String userId : personList) {
            record.setUserid(userId);
            dslContext.insertInto(CALLBACK_DIST)
                    .set(record)
                    .execute();
        }
    }

    public Field<Integer> getNextSequence() {
        final CallbackDist metaTable = CALLBACK_DIST.as("SEQUENCE_SEED");

        return DSL.select(DSL.ifnull(DSL.max(metaTable.SEQ), 0).add(1)).from(metaTable).asField();
    }

    public void redistribution(CallbackRedistFormRequest form) {
        for (int i = 0; i < form.getServiceSequences().size(); i++) {
            final Integer seq = form.getServiceSequences().get(i);

            final CallbackEntity orgCallback = callbackRepository.findOne(seq);

            dsl.update(CALLBACK_LIST)
                    .set(CALLBACK_LIST.USERID, form.getUsers().get(i % form.getUsers().size()))
                    .where(CALLBACK_LIST.COMPANY_ID.eq(getCompanyId()))
                    .and(CALLBACK_LIST.SEQ.eq(seq))
                    .execute();

            todoListRepository.updateCallbackTodo(form.getUsers().get(i % form.getUsers().size()), orgCallback.getCallbackNumber(), seq);
        }

        webSecureHistoryRepository.insert(WebSecureActionType.CALLBACK, WebSecureActionSubType.REDIST, subStringBytes(String.join("|", form.getUsers()), 400));
    }
}
