package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.CsRoute;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.form.CsRouteFormRequest;
import kr.co.eicn.ippbx.server.model.search.CsRouteSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.CS_ROUTE;

@Getter
@Repository
public class CsRouteRepository extends EicnBaseRepository<CsRoute, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CsRoute, Integer>{
    protected final Logger logger = LoggerFactory.getLogger(CsRouteRepository.class);
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public CsRouteRepository(CacheService cacheService, PBXServerInterface pbxServerInterface) {
        super(CS_ROUTE, CS_ROUTE.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CsRoute.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CsRoute> pagination(CsRouteSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(CsRouteSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotEmpty(search.getHuntNumber())) {
            conditions.add(CS_ROUTE.HUNT_NUMBER.eq(search.getHuntNumber()));
        }

        return conditions;
    }

    public Record insertOnGeneratedKey(CsRouteFormRequest form){
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CsRoute record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CsRoute();

        record.setHuntNumber(form.getQueueNumber());
        record.setCycle(form.getCycle());
        record.setCompanyId(getCompanyId());

        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(getCompanyId());

        pbxServerList.forEach(e -> {
            try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                super.insertOnGeneratedKey(pbxDsl, record);
            }
        });

        return super.insertOnGeneratedKey(record);
    }

    public boolean existsHunt(String huntNumber) {
        return Objects.nonNull(findOne(CS_ROUTE.HUNT_NUMBER.eq(huntNumber)));
    }
}
