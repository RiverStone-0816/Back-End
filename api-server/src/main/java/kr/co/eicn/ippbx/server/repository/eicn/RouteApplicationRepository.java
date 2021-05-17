package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.enums.RouteApplicationResult;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.RouteApplication;
import kr.co.eicn.ippbx.server.model.entity.eicn.RouteApplicationEntity;
import kr.co.eicn.ippbx.server.model.form.GradeListFormRequest;
import kr.co.eicn.ippbx.server.model.form.RAFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.RouteApplicationFormRequest;
import kr.co.eicn.ippbx.server.model.search.RouteApplicationSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RouteApplication.ROUTE_APPLICATION;

@Getter
@Repository
public class RouteApplicationRepository extends EicnBaseRepository<RouteApplication, RouteApplicationEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(RouteApplicationRepository.class);

    final PersonList appUser = PERSON_LIST.as("app_user");
    final PersonList rstUser = PERSON_LIST.as("rst_user");

    private final GradeListRepository gradeListRepository;
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public RouteApplicationRepository(GradeListRepository gradeListRepository, CacheService cacheService, PBXServerInterface pbxServerInterface) {
        super(ROUTE_APPLICATION, ROUTE_APPLICATION.SEQ, RouteApplicationEntity.class);
        this.gradeListRepository = gradeListRepository;
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(appUser).on(appUser.ID.eq(ROUTE_APPLICATION.APP_USERID))
                .leftJoin(rstUser).on(rstUser.ID.eq(ROUTE_APPLICATION.RST_USERID))
                .where();
    }

    @Override
    protected RecordMapper<Record, RouteApplicationEntity> getMapper() {
        return record -> {
            final RouteApplicationEntity entity = record.into(ROUTE_APPLICATION).into(RouteApplicationEntity.class);
            entity.setAppUserName(record.into(appUser.ID_NAME).value1());
            entity.setRstUserName(record.into(rstUser.ID_NAME).value1());
            return entity;
        };
    }

    public Pagination<RouteApplicationEntity> pagination(RouteApplicationSearchRequest search) {
        return super.pagination(search, conditions(search), Collections.singletonList(ROUTE_APPLICATION.INPUT_DATE.desc()));
    }

    private List<Condition> conditions(RouteApplicationSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(DSL.date(ROUTE_APPLICATION.INPUT_DATE).greaterOrEqual(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(DSL.date(ROUTE_APPLICATION.INPUT_DATE).lessOrEqual(search.getEndDate()));
        if (StringUtils.isNotEmpty(search.getNumber()))
            conditions.add(ROUTE_APPLICATION.NUMBER.like("%" + search.getNumber() + "%"));
        if (Objects.nonNull(search.getType()))
            conditions.add(ROUTE_APPLICATION.TYPE.eq(search.getType()));
        if (StringUtils.isNotEmpty(search.getAppUserId()))
            conditions.add(appUser.ID.like("%" + search.getAppUserId() + "%"));
        if (StringUtils.isNotEmpty(search.getAppUserName()))
            conditions.add(appUser.ID_NAME.like("%" + search.getAppUserName() + "%"));
        if (StringUtils.isNotEmpty(search.getRstUserId()))
            conditions.add(rstUser.ID.like("%" + search.getRstUserId() + "%"));
        if (StringUtils.isNotEmpty(search.getRstUserName()))
            conditions.add(rstUser.ID_NAME.like("%" + search.getRstUserName() + "%"));
        if (Objects.nonNull(search.getResult()))
            conditions.add(ROUTE_APPLICATION.RESULT.eq(search.getResult()));

        return conditions;
    }

    public void accept(RAFormUpdateRequest form, Integer seq) {
        final RouteApplicationEntity routeApplication = findOne(seq);

        final GradeListFormRequest gradeListForm = new GradeListFormRequest();
        gradeListForm.setGradeNumber(routeApplication.getNumber());
        gradeListForm.setGrade(String.valueOf(routeApplication.getType()));
        gradeListForm.setType(form.getRouteType());
        gradeListForm.setQueueNumber(form.getRouteQueueNumber());

        gradeListRepository.insertOnGeneratedKey(gradeListForm);

        routeApplication.setResultDate(new Timestamp(System.currentTimeMillis()));
        routeApplication.setRstUserid(g.getUser().getId());
        routeApplication.setResult(RouteApplicationResult.ACCEPT);

        updateByKey(routeApplication, seq);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.updateByKey(pbxDsl, routeApplication, seq);
            }
        });

//        dsl.update(ROUTE_APPLICATION)
//                .set(ROUTE_APPLICATION.RESULT_DATE, DSL.now())
//                .set(ROUTE_APPLICATION.RST_USERID, g.getUser().getId())
//                .set(ROUTE_APPLICATION.RESULT, RouteApplicationResult.ACCEPT)
//                .where(ROUTE_APPLICATION.SEQ.eq(seq))
//                .execute();
    }

    public void reject(Integer seq) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RouteApplication record = findOneIfNullThrow(seq);
        record.setResultDate(new Timestamp(System.currentTimeMillis()));
        record.setRstUserid(g.getUser().getId());
        record.setResult(RouteApplicationResult.REJECT);

        super.updateByKey(record, seq);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.updateByKey(pbxDsl, record, seq);
            }
        });

//        dsl.update(ROUTE_APPLICATION)
//                .set(ROUTE_APPLICATION.RESULT_DATE, DSL.now())
//                .set(ROUTE_APPLICATION.RST_USERID, g.getUser().getId())
//                .set(ROUTE_APPLICATION.RESULT, RouteApplicationResult.REJECT)
//                .where(ROUTE_APPLICATION.SEQ.eq(seq))
//                .execute();
    }

    public void insert(RouteApplicationFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RouteApplication record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RouteApplication();
        record.setInputDate(new Timestamp(System.currentTimeMillis()));
        record.setType(form.getType());
        record.setNumber(form.getNumber());
        record.setMemo(form.getMemo());
        record.setUniqueid(form.getUniqueId());
        record.setResult(RouteApplicationResult.NONE);
        record.setAppUserid(g.getUser().getId());
        record.setCompanyId(getCompanyId());

        super.insertOnGeneratedKey(record);

        cacheService.pbxServerList(getCompanyId()).forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.insert(pbxDsl, record);
            }
        });

//        dsl.insertInto(ROUTE_APPLICATION)
//                .set(ROUTE_APPLICATION.INPUT_DATE, DSL.now())
//                .set(ROUTE_APPLICATION.TYPE, form.getType())
//                .set(ROUTE_APPLICATION.NUMBER, form.getNumber())
//                .set(ROUTE_APPLICATION.MEMO, form.getMemo())
//                .set(ROUTE_APPLICATION.UNIQUEID, form.getUniqueId())
//                .set(ROUTE_APPLICATION.RESULT, RouteApplicationResult.NONE)
//                .set(ROUTE_APPLICATION.APP_USERID, g.getUser().getId())
//                .set(ROUTE_APPLICATION.COMPANY_ID, getCompanyId())
//                .returning(ROUTE_APPLICATION.SEQ)
//                .execute();
    }

}
