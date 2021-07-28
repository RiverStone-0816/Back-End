package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.dto.customdb.MainReceivePathResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchNumber070Response;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.Number070Entity;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleInfoEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.form.NumberTypeChangeRequest;
import kr.co.eicn.ippbx.model.search.NumberSearchRequest;
import kr.co.eicn.ippbx.model.search.ScheduleInfoSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchNumber070Request;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroup.SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleInfo.SCHEDULE_INFO;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class Number070Repository extends EicnBaseRepository<Number_070, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070, String> {
    protected final Logger logger = LoggerFactory.getLogger(Number070Repository.class);

    private final ScheduleGroupRepository scheduleGroupRepository;
    private final OrganizationService organizationService;
    private final PBXServerInterface pbxServerInterface;
    private final CacheService cacheService;

    public Number070Repository(ScheduleGroupRepository scheduleGroupRepository, OrganizationService organizationService, PBXServerInterface pbxServerInterface, CacheService cacheService) {
        super(NUMBER_070, NUMBER_070.NUMBER, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070.class);
        orderByFields.add(NUMBER_070.NUMBER.asc());

        this.scheduleGroupRepository = scheduleGroupRepository;
        this.organizationService = organizationService;
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> findAll(NumberSearchRequest search) {
        return super.findAll(typeConditions(search));
    }

    public Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> findByAllListCovertToMap(Condition condition) {
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> entities = findAll(condition);
        return entities.stream()
                .collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber, e -> e));
    }

    public void typeChange(NumberTypeChangeRequest form, String number) {
        this.updateByKey(form, number);

        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        this.updateByKey(pbxDsl, form, number);
                    }
                });
    }

    public void updateStatusAllPbxServers(final String number, final byte status) {
        updateStatus(dsl, number, status);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                updateStatus(pbxDsl, number, status);
            }
        });
    }

    public void updateStatus(DSLContext dslContext, final String number, final byte status) {
        dslContext.update(NUMBER_070)
                .set(NUMBER_070.STATUS, status)
                .where(getCondition(number))
                .execute();
    }

    public List<SearchNumber070Response> search(SearchNumber070Request search) {
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> numbers = findAll(searchConditions(search));
        final List<CompanyServerEntity> companyServerEntities = cacheService.getCompanyServerList(g.getUser().getCompanyId());

        return numbers.stream()
                .map(e -> {
                    final SearchNumber070Response response = modelMapper.map(e, SearchNumber070Response.class);
                    companyServerEntities.stream()
                            .filter(company -> company.getServer().getHost().equals(e.getHost()))
                            .findAny()
                            .ifPresent(r -> response.setHostName(r.getServer().getName()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    private List<Condition> typeConditions(NumberSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(NUMBER_070.TYPE.eq(search.getType()));
        conditions.add((NUMBER_070.KIND.notEqual("B")));

        return conditions;
    }

    private List<Condition> searchConditions(SearchNumber070Request search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getType()))
            conditions.add(NUMBER_070.TYPE.eq(search.getType()));
        if (Objects.nonNull(search.getStatus()))
            conditions.add(NUMBER_070.STATUS.eq(search.getStatus()));

        return conditions;
    }

    //상담화면 수신경로
    public List<MainReceivePathResponse> findOneByNumbers(String iniNum, String secondNum) {
        return dsl.select(NUMBER_070.NUMBER.as("number"))
                .select(NUMBER_070.TYPE.as("type"))
                .from(NUMBER_070)
                .where(compareCompanyId())
                .and(NUMBER_070.NUMBER.eq(iniNum))
                .or(NUMBER_070.NUMBER.eq(secondNum == null ? "" : secondNum))
                .fetchInto(MainReceivePathResponse.class);

    }

    public List<Number070Entity> getScheduleLists(ScheduleInfoSearchRequest search) {
        final SelectConditionStep<Record> query = dsl.select()
                .from(NUMBER_070)
                .leftOuterJoin(SCHEDULE_INFO)
                .on(NUMBER_070.NUMBER.eq(SCHEDULE_INFO.NUMBER).and(SCHEDULE_INFO.COMPANY_ID.eq(getCompanyId())))
                .leftOuterJoin(SCHEDULE_GROUP)
                .on(SCHEDULE_INFO.GROUP_ID.eq(SCHEDULE_GROUP.PARENT)
                        .and(SCHEDULE_GROUP.COMPANY_ID.eq(getCompanyId())))
                .where(compareCompanyId());

        if (Objects.equals(ScheduleType.WEEK, search.getType()))
            query.and(SCHEDULE_INFO.WEEK.notEqual(EMPTY));
        if (isNotEmpty(search.getNumber()))
            query.and(NUMBER_070.NUMBER.eq(search.getNumber()));
        if (Objects.nonNull(search.getGroupId()))
            query.and(SCHEDULE_INFO.GROUP_ID.eq(search.getGroupId()));
        if (isNotEmpty(search.getSearchDate())) {
            if (Objects.equals(ScheduleType.WEEK, search.getType()))
                query.and(SCHEDULE_INFO.WEEK.like("%" + search.getSearchDate()));
            else if (Objects.equals(ScheduleType.DAY, search.getType()))
                query.and(SCHEDULE_INFO.FROMDATE.greaterOrEqual(DSL.date(search.getSearchDate()))
                        .and(SCHEDULE_INFO.TODATE.lessOrEqual(DSL.date(search.getSearchDate()))));
        }
        if (isNotEmpty(search.getGroupCode()))
            query.and(SCHEDULE_INFO.GROUP_CODE.like("%" + search.getGroupCode() + "%"));
        if (search.getType() != null)
            query.and(SCHEDULE_INFO.TYPE.eq(search.getType().getCode()));

        final Map<Record, Result<Record>> recordResultMap = query
                .orderBy(NUMBER_070.TYPE.desc(), NUMBER_070.NUMBER, SCHEDULE_INFO.WEEK)
                .fetch()
                .intoGroups(NUMBER_070.fields());

        final List<Number070Entity> number070Entities = new ArrayList<>();
        final List<ScheduleGroupEntity> scheduleGroupLists = scheduleGroupRepository.getScheduleGroupLists();
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

        recordResultMap.forEach((record, records) -> {
            final Number070Entity number070Entity = record.into(Number070Entity.class);

            number070Entity.setScheduleInfos(records.stream()
                    .filter(r -> r.getValue(SCHEDULE_INFO.SEQ) != null)
                    .map(r -> {
                        final ScheduleInfoEntity into = r.into(ScheduleInfoEntity.class);
                        final ScheduleGroupEntity scheduleGroupEntity = r.into(ScheduleGroupEntity.class);

                        scheduleGroupLists.stream()
                                .filter(e -> e.getParent().equals(scheduleGroupEntity.getParent()))
                                .findAny()
                                .ifPresent(e -> scheduleGroupEntity.setScheduleGroupLists(e.getScheduleGroupLists()));

                        if (isNotEmpty(into.getGroupCode()))
                            into.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, into.getGroupCode())
                                    .stream()
                                    .map(group -> modelMapper.map(group, OrganizationSummaryResponse.class))
                                    .collect(Collectors.toList()));

                        into.setScheduleGroup(scheduleGroupEntity);

                        return into;
                    })
                    .collect(Collectors.toList()));

            number070Entities.add(number070Entity);
        });

        return number070Entities;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> prvGroupNumber070() {
        return findAll(NUMBER_070.NUMBER.like("070%"));
    }
}
