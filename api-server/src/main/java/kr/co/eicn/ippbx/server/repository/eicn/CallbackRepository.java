package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.CallbackList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.server.model.entity.eicn.CallbackEntity;
import kr.co.eicn.ippbx.server.model.enums.CallbackStatus;
import kr.co.eicn.ippbx.server.model.enums.IdType;
import kr.co.eicn.ippbx.server.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.server.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.server.model.form.CallbackListUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.search.CallbackHistorySearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.*;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ServiceList.SERVICE_LIST;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.currentDate;

@Getter
@Repository
public class CallbackRepository extends EicnBaseRepository<CallbackList, CallbackEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CallbackRepository.class);

    private final TodoListRepository todoListRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;

    CallbackRepository(TodoListRepository todoListRepository, WebSecureHistoryRepository webSecureHistoryRepository) {
        super(CALLBACK_LIST, CALLBACK_LIST.SEQ, CallbackEntity.class);
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        addField(QUEUE_NAME.HAN_NAME);
        addField(CALLBACK_LIST);
        addField(SERVICE_LIST.SVC_NAME);
        addField(PERSON_LIST.ID_NAME);
        this.todoListRepository = todoListRepository;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        query.groupBy(CALLBACK_LIST.SEQ);
        return query
                .leftJoin(SERVICE_LIST).on(SERVICE_LIST.SVC_NUMBER.eq(CALLBACK_LIST.SERVICE_NUMBER))
                .leftJoin(PERSON_LIST).on(PERSON_LIST.ID.eq(CALLBACK_LIST.USERID))
                .leftJoin(QUEUE_MEMBER_TABLE).on(QUEUE_MEMBER_TABLE.MEMBERNAME.eq(PERSON_LIST.PEER))
                .leftJoin(QUEUE_NAME).on(QUEUE_NAME.NUMBER.eq(CALLBACK_LIST.HUNT_NUMBER))
                .where(compareCompanyId());
    }

    @Override
    protected RecordMapper<Record, CallbackEntity> getMapper() {
        return record -> {
            final CallbackEntity entity = record.into(CALLBACK_LIST).into(CallbackEntity.class);

            entity.setHuntName(record.into(QUEUE_NAME).into(QueueName.class).getHanName());
            entity.setIdName(record.into(PERSON_LIST).into(PersonList.class).getIdName());
            entity.setSvcName(record.into(SERVICE_LIST).into(ServiceList.class).getSvcName());

            return entity;
        };
    }

    public Pagination<CallbackEntity> pagination(CallbackHistorySearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public List<CallbackEntity> findAll(CallbackHistorySearchRequest search) {
        return super.findAll(conditions(search));
    }

    public void update(CallbackListUpdateFormRequest form) {
        List<Integer> seqs = form.getServiceSequences();

        for (Integer seq : seqs) {
            dsl.update(CALLBACK_LIST)
                    .set(CALLBACK_LIST.STATUS, form.getStatus().getCode())
                    .set(CALLBACK_LIST.RESULTDATE, DSL.now())
                    .where(CALLBACK_LIST.SEQ.eq(seq))
                    .execute();

            final TodoList todoList = todoListRepository.findOneByCallbackSeq(String.valueOf(seq));

            if (todoList != null && Objects.equals(form.getStatus(), CallbackStatus.COMPLETE)) {
                dsl.update(TODO_LIST)
                        .set(TODO_LIST.UPDATEDATE, DSL.currentTimestamp())
                        .set(TODO_LIST.TODO_STATUS, TodoListTodoStatus.DONE)
                        .where(TODO_LIST.SEQ.eq(todoList.getSeq()))
                        .execute();
            } else if (todoList != null && Objects.equals(form.getStatus(), CallbackStatus.PROCESSING)) {
                dsl.update(TODO_LIST)
                        .set(TODO_LIST.UPDATEDATE, DSL.currentTimestamp())
                        .set(TODO_LIST.TODO_STATUS, TodoListTodoStatus.ING)
                        .where(TODO_LIST.SEQ.eq(todoList.getSeq()))
                        .execute();
            }
        }
        webSecureHistoryRepository.insert(WebSecureActionType.CALLBACK, WebSecureActionSubType.MOD, form.getStatus().getCode() + seqs.size());
    }

    public void deleteByPk(List<Integer> seq) {
        for (Integer el : seq) {
            final TodoList todoList = todoListRepository.findOneByCallbackSeq(String.valueOf(el));

            if (todoList != null) {
                dsl.update(TODO_LIST)
                        .set(TODO_LIST.UPDATEDATE, DSL.currentTimestamp())
                        .set(TODO_LIST.TODO_STATUS, TodoListTodoStatus.DELETE)
                        .where(TODO_LIST.SEQ.eq(todoList.getSeq()))
                        .execute();
            }
            deleteOnIfNullThrow(el);
        }
        webSecureHistoryRepository.insert(WebSecureActionType.CALLBACK, WebSecureActionSubType.DEL,
                TodoListTodoStatus.DELETE.getName() + seq.size());
    }

    private List<Condition> conditions(CallbackHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getStartDate() != null)
            conditions.add(CALLBACK_LIST.INPUTDATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));
        if (search.getEndDate() != null)
            conditions.add(CALLBACK_LIST.INPUTDATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if (StringUtils.isNotEmpty(search.getSvcNumber()))
            conditions.add(SERVICE_LIST.SVC_NUMBER.eq(search.getSvcNumber()));

        if (!Objects.isNull(search.getStatus())) {
            conditions.add(CALLBACK_LIST.STATUS.eq(search.getStatus().getCode()));
        }

        if (StringUtils.isNotEmpty(search.getCallbackNumber()))
            conditions.add(CALLBACK_LIST.CALLBACK_NUMBER.like(  "%" + search.getCallbackNumber() + "%")
                    .or(CALLBACK_LIST.CALLER_NUMBER.like("%" + search.getCallbackNumber() + "%")));

        if (g.getUser().getIdType().equals(IdType.USER.getCode()))
            conditions.add(CALLBACK_LIST.USERID.eq(g.getUser().getId()));

        return conditions;
    }

    public List<CallbackEntity> search(CallbackHistorySearchRequest search) {
        return findAll(conditions(search));
    }

    public void completeCallback(String userId, String phone) {
        dsl.update(CALLBACK_LIST)
                .set(CALLBACK_LIST.STATUS, CallbackStatus.COMPLETE.getCode())
                .set(CALLBACK_LIST.EXE_USERID, userId)
                .set(CALLBACK_LIST.RESULTDATE, DSL.currentTimestamp())
                .where(CALLBACK_LIST.STATUS.eq(CallbackStatus.NONE.getCode()))
                .and(CALLBACK_LIST.CALLER_NUMBER.eq(phone))
                .and(CALLBACK_LIST.USERID.eq(userId))
                .execute();
    }

    public Integer getTodayIncompleteCallbackCount() {
        return dsl.select(count())
                .from(CALLBACK_LIST)
                .where(compareCompanyId())
                .and(CALLBACK_LIST.INPUTDATE.ge(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))))
                .and(CALLBACK_LIST.STATUS.ne(CallbackStatus.COMPLETE.getCode()))
                .fetchOneInto(Integer.class);
    }
}