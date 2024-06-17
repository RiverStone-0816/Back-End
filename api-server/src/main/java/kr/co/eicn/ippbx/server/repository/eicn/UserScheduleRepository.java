package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.UserScheduleType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.UserSchedule;
import kr.co.eicn.ippbx.model.entity.eicn.UserScheduleEntity;
import kr.co.eicn.ippbx.model.form.UserScheduleFormRequest;
import kr.co.eicn.ippbx.model.search.UserScheduleSearchRequest;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.USER_SCHEDULE;

@Getter
@Repository
public class UserScheduleRepository extends EicnBaseRepository<UserSchedule, UserScheduleEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(UserScheduleRepository.class);

    public UserScheduleRepository() {
        super(USER_SCHEDULE, USER_SCHEDULE.SEQ, UserScheduleEntity.class);
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(PERSON_LIST).on(PERSON_LIST.ID.eq(USER_SCHEDULE.USERID))
                .where();
    }

    @Override
    protected RecordMapper<Record, UserScheduleEntity> getMapper() {
        return record -> {
            final UserScheduleEntity entity = record.into(USER_SCHEDULE).into(UserScheduleEntity.class);
            entity.setUserName(record.into(PERSON_LIST).getIdName());
            return entity;
        };
    }

    public List<UserScheduleEntity> search(UserScheduleSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(UserScheduleSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(USER_SCHEDULE.TYPE.eq(UserScheduleType.ALL)
                .or(USER_SCHEDULE.USERID.eq(g.getUser().getId())));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (search.getYear() == null)
            search.setYear(calendar.get(Calendar.YEAR));

        if (search.getMonth() == null)
            search.setMonth(calendar.get(Calendar.MONTH) + 1);


        calendar.setTimeInMillis(0);
        calendar.set(Calendar.YEAR, search.getYear());
        calendar.set(Calendar.MONTH, search.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        final Timestamp checkingStartTime = new Timestamp(calendar.getTimeInMillis());

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        final Timestamp checkingEndTime = new Timestamp(calendar.getTimeInMillis());

        // 날짜가 겹치는지 확인하는 로직: !((interval A's end < interval B's start) || (interval B's end < interval A's start))
        conditions.add((USER_SCHEDULE.END.lessThan(checkingStartTime).or(USER_SCHEDULE.START.greaterThan(checkingEndTime))).not());

        return conditions;
    }

    public void insert(UserScheduleFormRequest form) {
        dsl.insertInto(USER_SCHEDULE)
                .set(USER_SCHEDULE.TYPE, form.getType())
                .set(USER_SCHEDULE.IMPORTANT, form.getImportant())
                .set(USER_SCHEDULE.START, form.getStart())
                .set(USER_SCHEDULE.END, form.getEnd())
                .set(USER_SCHEDULE.TITLE, form.getTitle())
                .set(USER_SCHEDULE.CONTENTS, form.getContents())
                .set(USER_SCHEDULE.USERID, g.getUser().getId())
                .set(USER_SCHEDULE.COMPANY_ID, g.getUser().getCompanyId())
                .execute();
    }

    public void update(Integer seq, UserScheduleFormRequest form) {
        dsl.update(USER_SCHEDULE)
                .set(USER_SCHEDULE.TYPE, form.getType())
                .set(USER_SCHEDULE.IMPORTANT, form.getImportant())
                .set(USER_SCHEDULE.START, form.getStart())
                .set(USER_SCHEDULE.END, form.getEnd())
                .set(USER_SCHEDULE.TITLE, form.getTitle())
                .set(USER_SCHEDULE.CONTENTS, form.getContents())
                .where(compareCompanyId())
                .and(USER_SCHEDULE.SEQ.eq(seq))
                .and(USER_SCHEDULE.USERID.eq(g.getUser().getId()))
                .execute();
    }

    public void deleteSchedule(Integer seq) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(USER_SCHEDULE.SEQ.eq(seq));
        conditions.add(USER_SCHEDULE.USERID.eq(g.getUser().getId()));
        delete(conditions);
    }
}
