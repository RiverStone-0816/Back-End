package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueNameResponse;
import kr.co.eicn.ippbx.model.search.StatHuntSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchQueueNameRequest;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueMemberTable.QUEUE_MEMBER_TABLE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.QUEUE_NAME;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.jooq.impl.DSL.noCondition;

@Getter
@Repository
public class QueueNameRepository extends EicnBaseRepository<QueueName, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName, String> {
    protected final Logger logger = LoggerFactory.getLogger(QueueNameRepository.class);

    public QueueNameRepository() {
        super(QUEUE_NAME, QUEUE_NAME.NAME, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName.class);

        orderByFields.add(QUEUE_NAME.HAN_NAME.asc());
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName findOneByQueueNumber(String queueNumber) {
        return findOne(QUEUE_NAME.COMPANY_ID.eq(getCompanyId()).and(QUEUE_NAME.NUMBER.eq(queueNumber)));
    }

    public List<SearchQueueNameResponse> search(SearchQueueNameRequest search) {
        return findAll(searchConditions(search)).stream()
                .map(e -> modelMapper.map(e, SearchQueueNameResponse.class))
                .collect(Collectors.toList());
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName> getQueueNameListByService(StatHuntSearchRequest search, String groupTreeName) {
        return findAll(conditions(search, groupTreeName));
    }

    private List<Condition> searchConditions(SearchQueueNameRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }

    private List<Condition> conditions(StatHuntSearchRequest search, String groupTreeName) {
        final List<Condition> conditions = new ArrayList<>();
        Condition serviceCondition = noCondition();

        if (g.getUser().getDataSearchAuthorityType() != null) {
            switch (g.getUser().getDataSearchAuthorityType()) {
                case NONE:
                    conditions.add(DSL.falseCondition());
                    return conditions;
                case MINE:
                case GROUP:
                    conditions.add(QUEUE_NAME.GROUP_TREE_NAME.like(g.getUser().getGroupTreeName() + "%"));
                    break;
            }
        }

        for (String serviceNumber : search.getServiceNumbers()) {
            serviceCondition = serviceCondition.or(QUEUE_NAME.SVC_NUMBER.eq(serviceNumber));
        }

        if (StringUtils.isNotEmpty(groupTreeName))
            conditions.add(QUEUE_NAME.GROUP_TREE_NAME.like(groupTreeName + '%'));

        conditions.add(serviceCondition);

        return conditions;
    }

    public void insert(DSLContext dslContext, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName record) {
        dslContext.insertInto(QUEUE_NAME)
                .set(dslContext.newRecord(QUEUE_NAME, record))
                .set(QUEUE_NAME.CNT, 0)
                .set(QUEUE_NAME.BLENDING_MODE, "N")
                .set(QUEUE_NAME.BLENDING_WAIT_EXE_YN, "N")
                .set(QUEUE_NAME.BLENDING_WAIT_CNT, 1)
                .set(QUEUE_NAME.BLENDING_WAIT_KEEPTIME, 5)
                .set(QUEUE_NAME.BLENDING_TIME_FROMTIME, 0)
                .set(QUEUE_NAME.BLENDING_TIME_TOTIME, 0)
                .set(QUEUE_NAME.BLENDING_WAIT_LASTTIME, DSL.now())
                .set(QUEUE_NAME.COMPANY_ID, getCompanyId())
                .execute();
    }

    /**
     * 헌트별 멤버상태 수
     */
    public List<QueueMemberStatus> memberStatusByQueues() {
        return dsl.select(QUEUE_NAME.NAME
                    , QUEUE_NAME.NUMBER
                    , QUEUE_NAME.HAN_NAME
                    , QUEUE_MEMBER_TABLE.PAUSED
                    , QUEUE_MEMBER_TABLE.IS_LOGIN
                    , DSL.count().as("cnt")
                )
                .from(QUEUE_NAME)
                .leftJoin(QUEUE_MEMBER_TABLE)
                .on(QUEUE_NAME.NAME.eq(QUEUE_MEMBER_TABLE.QUEUE_NAME)
                    .and(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(getCompanyId())))
                .where(compareCompanyId())
                .groupBy(QUEUE_NAME.NAME, QUEUE_NAME.NUMBER, QUEUE_NAME.HAN_NAME, QUEUE_MEMBER_TABLE.PAUSED, QUEUE_MEMBER_TABLE.IS_LOGIN)
                .fetchInto(QueueMemberStatus.class);
    }

    /**
     * 서비스별 멤버상태
     */
    public List<QueueMemberStatus> memberStatusByServices() {
        return dsl.select(QUEUE_NAME.NAME
                    , QUEUE_NAME.NUMBER
                    , QUEUE_NAME.SVC_NUMBER
                    , QUEUE_NAME.HAN_NAME
                    , QUEUE_MEMBER_TABLE.PAUSED
                    , QUEUE_MEMBER_TABLE.IS_LOGIN
                    , DSL.count().as("cnt")
                )
                .from(QUEUE_NAME)
                .leftJoin(QUEUE_MEMBER_TABLE)
                .on(QUEUE_NAME.NAME.eq(QUEUE_MEMBER_TABLE.QUEUE_NAME)
                    .and(QUEUE_MEMBER_TABLE.COMPANY_ID.eq(getCompanyId())))
                .where(compareCompanyId())
                .and(QUEUE_NAME.SVC_NUMBER.notEqual(EMPTY))
                .groupBy(QUEUE_NAME.NAME, QUEUE_NAME.NUMBER, QUEUE_NAME.SVC_NUMBER, QUEUE_NAME.HAN_NAME, QUEUE_MEMBER_TABLE.PAUSED, QUEUE_MEMBER_TABLE.IS_LOGIN)
                .fetchInto(QueueMemberStatus.class);
    }

    //상담화면 수신경로
    public String getHanNameByNumber(String number) {
        return dsl.select(QUEUE_NAME.HAN_NAME)
                .from(QUEUE_NAME)
                .where(compareCompanyId())
                .and(QUEUE_NAME.NUMBER.eq(number))
                .fetchOneInto(String.class);
    }

    //<영어명, 한글명>
    public Map<String, String> getHuntNameMap() {
        return findAll().stream().collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName::getName, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName::getHanName));
    }

    @Data
    public static class QueueMemberStatus {
        private String name;
        private String number;
        private String svcNumber;
        private String hanName;
        private Integer paused;
        private String isLogin;
        private Integer cnt;
    }
}
