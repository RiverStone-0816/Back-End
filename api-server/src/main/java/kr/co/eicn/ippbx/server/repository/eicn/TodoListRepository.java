package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.Tables;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoKind;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TodoList;
import kr.co.eicn.ippbx.model.dto.eicn.TodoDataResponse;
import kr.co.eicn.ippbx.model.form.TodoListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.TodoReservationFormRequest;
import kr.co.eicn.ippbx.model.search.TodoListSearchRequest;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TodoList.TODO_LIST;
import static org.jooq.impl.DSL.now;

@Getter
@Repository
public class TodoListRepository extends EicnBaseRepository<TodoList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(TodoListRepository.class);

    public TodoListRepository() {
        super(TODO_LIST, TODO_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList.class);
    }

    public TodoDataResponse getTodoMonitor(TodoListTodoKind kind) {
        final LocalDate toDay = LocalDate.now();
        TodoDataResponse res = null;
        res = dsl.select(TODO_LIST.TODO_KIND)
                .select(DSL.count(TODO_LIST.TODO_KIND).as("total"))
                .select(DSL.count(DSL.when(TODO_LIST.TODO_STATUS.eq(TodoListTodoStatus.DONE), 1)).as("success"))
                .from(TODO_LIST)
                .where(TODO_LIST.USERID.eq(g.getUser().getId()))
                .and(TODO_LIST.REGDATE.ge(Timestamp.valueOf(toDay.toString() + " 00:00:00")))
                .and(TODO_LIST.REGDATE.le(Timestamp.valueOf(toDay.toString() + " 23:59:59")))
                .and(TODO_LIST.TODO_KIND.eq(kind))
//				.groupBy(TODO_LIST.TODO_KIND)
                .fetchOneInto(TodoDataResponse.class); // 이 부분은 수정해야됩니다.
        if (res.getTodoKind() == null) {
            res.setTodoKind(kind);
        }
//		if(res.getTotal() != 0) {
//			res.setSuccessRate((res.getTotal() / res.getSuccess()) * 100);
        res.setSuccessRate(EicnUtils.getRateValue(res.getSuccess(), res.getTotal()));
//		}
        return res;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList> findRecentReservations() {
        final int ONE_MINUTE = 60 * 1000;
        return findAll(TODO_LIST.USERID.eq(g.getUser().getId())
                .and(DSL.cast(TODO_LIST.DETAIL_CONNECT_INFO, Long.class).ge(System.currentTimeMillis() - ONE_MINUTE))
                .and(DSL.cast(TODO_LIST.DETAIL_CONNECT_INFO, Long.class).le(System.currentTimeMillis() + ONE_MINUTE))
                .and(TODO_LIST.TODO_STATUS.eq(TodoListTodoStatus.ING))
        );
    }

    public void reserve(TodoReservationFormRequest form) {
        dsl.insertInto(TODO_LIST)
                .set(TODO_LIST.REGDATE, form.getReservationTime())
                .set(TODO_LIST.UPDATEDATE, now())
                .set(TODO_LIST.USERID, g.getUser().getId())
                .set(TODO_LIST.TODO_KIND, TodoListTodoKind.RESERVE)
                .set(TODO_LIST.TODO_STATUS, TodoListTodoStatus.ING)
                .set(TODO_LIST.TODO_INFO, form.getCustomNumber())
                .set(TODO_LIST.DETAIL_CONNECT_INFO, "" + form.getReservationTime().getTime())
                .set(TODO_LIST.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void insertTransferData(Integer seq, String userId, String customerInfo) {
        dsl.insertInto(TODO_LIST)
                .set(TODO_LIST.REGDATE, now())
                .set(TODO_LIST.UPDATEDATE, now())
                .set(TODO_LIST.USERID, userId)
                .set(TODO_LIST.TODO_KIND, TodoListTodoKind.TRANSFER)
                .set(TODO_LIST.TODO_INFO, customerInfo)
                .set(TODO_LIST.TODO_STATUS, TodoListTodoStatus.ING)
                .set(TODO_LIST.DETAIL_CONNECT_INFO, String.valueOf(seq))
                .set(TODO_LIST.COMPANY_ID, getCompanyId())
                .execute();
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList findOneByCallbackSeq(String detailConnectInfo) {
        final Condition condition = TODO_LIST.TODO_KIND.eq(TodoListTodoKind.CALLBACK).and(TODO_LIST.DETAIL_CONNECT_INFO.eq(detailConnectInfo));
        return findOne(condition);
    }

    public void updateReserve(TodoReservationFormRequest form) {
        dsl.update(TODO_LIST)
                .set(TODO_LIST.UPDATEDATE, now())
                .set(TODO_LIST.TODO_STATUS, TodoListTodoStatus.DONE)
                .where(compareCompanyId())
                .and(TODO_LIST.TODO_KIND.eq(TodoListTodoKind.RESERVE))
                .and(TODO_LIST.USERID.eq(g.getUser().getId()))
                .and(TODO_LIST.TODO_INFO.eq(form.getCustomNumber()))
                .and(TODO_LIST.DETAIL_CONNECT_INFO.eq(form.getCustomNumber()))
                .execute();
    }

    public void updateStatusBySeq(TodoListUpdateFormRequest form) {
        dsl.update(Tables.TODO_LIST)
                .set(Tables.TODO_LIST.UPDATEDATE, DSL.currentTimestamp())
                .set(Tables.TODO_LIST.TODO_STATUS, form.getTodoStatus())
                .where(Tables.TODO_LIST.SEQ.eq(form.getSeq()))
                .execute();
    }

    public void updateCallbackTodo(String userId, String callbackNumber, Integer callbackSequence) {
        dsl.update(TODO_LIST)
                .set(TODO_LIST.UPDATEDATE, now())
                .set(TODO_LIST.USERID, userId)
                .where(compareCompanyId())
                .and(TODO_LIST.TODO_KIND.eq(TodoListTodoKind.CALLBACK))
                .and(TODO_LIST.TODO_INFO.eq(callbackNumber))
                .and(TODO_LIST.DETAIL_CONNECT_INFO.eq(String.valueOf(callbackSequence)))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList> findAll(TodoListSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(TodoListSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotEmpty(search.getUserId()))
            conditions.add(TODO_LIST.USERID.eq(search.getUserId()));

        if (!search.getStatuses().isEmpty())
            conditions.add(TODO_LIST.TODO_STATUS.in(search.getStatuses()));

        if (!search.getPhoneNumbers().isEmpty())
            conditions.add(TODO_LIST.TODO_INFO.in(search.getPhoneNumbers()));

        if (search.getStartDate() != null)
            conditions.add(TODO_LIST.REGDATE.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if (search.getEndDate() != null)
            conditions.add(TODO_LIST.REGDATE.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        return conditions;
    }
}
