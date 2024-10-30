package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatMemberStatus;
import kr.co.eicn.ippbx.model.entity.statdb.StatMemberStatusEntity;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static org.jooq.impl.DSL.*;

@Getter
public class StatMemberStatusRepository extends StatDBBaseRepository<CommonStatMemberStatus, StatMemberStatusEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatMemberStatusRepository.class);

    private final CommonStatMemberStatus TABLE;
    private       boolean                isTotal = false;

    public StatMemberStatusRepository(String companyName) {
        super(new CommonStatMemberStatus(companyName), new CommonStatMemberStatus(companyName).SEQ, StatMemberStatusEntity.class);
        TABLE = new CommonStatMemberStatus(companyName);

        addField(
                year(TABLE.STAT_DATE),
                month(TABLE.STAT_DATE),
                TABLE.STAT_DATE,
                TABLE.STAT_HOUR,
                dayOfWeek(TABLE.STAT_DATE),
                TABLE.USERID,
                TABLE.STATUS,
                ifnull(sum(TABLE.TOTAL), 0).as(TABLE.TOTAL),
                ifnull(sum(TABLE.DIFF_SUM), 0).as(TABLE.DIFF_SUM)
        );
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        if (isTotal)
            query.groupBy(TABLE.USERID);
        query.groupBy(TABLE.STATUS);

        setTimeUnit(query);

        return query
                .innerJoin(PERSON_LIST).on(PERSON_LIST.ID.eq(TABLE.USERID).and(PERSON_LIST.COMPANY_ID.eq(TABLE.COMPANY_ID)))
                .where();
    }

    public List<StatMemberStatusEntity> findAll(StatUserSearchRequest search) {
        isTotal = true;
        return findAll(conditions(search));
    }

    public List<StatMemberStatusEntity> findAllTotal(StatUserSearchRequest search) {
        isTotal = false;
        return findAll(conditions(search));
    }

    public List<Condition> conditions(StatUserSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        standardTime = search.getTimeUnit();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.STAT_DATE.ge(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.STAT_DATE.le(search.getEndDate()));

        if (CollectionUtils.isNotEmpty(search.getPersonIds()))
            conditions.add(TABLE.USERID.in(search.getPersonIds()));

        return conditions;
    }
}
