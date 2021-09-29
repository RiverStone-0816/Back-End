package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ExecutePdsGroup;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.search.ExecutePDSGroupSearchRequest;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.EXECUTE_PDS_GROUP;

@Getter
@Repository
public class ExecutePDSGroupRepository extends EicnBaseRepository<ExecutePdsGroup, ExecutePDSGroupEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ExecutePDSGroupRepository.class);

    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public ExecutePDSGroupRepository(CacheService cacheService, PBXServerInterface pbxServerInterface) {
        super(EXECUTE_PDS_GROUP, EXECUTE_PDS_GROUP.SEQ, ExecutePDSGroupEntity.class);
        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
    }

    public List<ExecutePDSGroupEntity> findAllGroupId(DSLContext dslContext, Integer groupId) {
        return findAll(dslContext, EXECUTE_PDS_GROUP.PDS_GROUP_ID.eq(groupId));
    }

    public List<Condition> conditions(ExecutePDSGroupSearchRequest search) {
        final List<org.jooq.Condition> conditions = new ArrayList<>();

        if (search.getCreatedStartDate() != null)
            conditions.add(EXECUTE_PDS_GROUP.START_DATE.greaterOrEqual(search.getCreatedStartDate()));
        if (search.getCreatedEndDate() != null)
            conditions.add(EXECUTE_PDS_GROUP.START_DATE.lessOrEqual(search.getCreatedEndDate()));

        return conditions;
    }

    public ExecutePDSGroupEntity findByRunHost(String runHost, String executeId) {
        CompanyServerEntity companyServerEntity = cacheService.pbxServerList(getCompanyId()).stream().filter(e -> e.getHost().equals(runHost)).findFirst().orElse(null);
        if (companyServerEntity != null) {
            DSLContext pbxDsl = pbxServerInterface.using(companyServerEntity.getHost(), "PDS");
            return findOne(pbxDsl, getCompanyId(), EXECUTE_PDS_GROUP.EXECUTE_ID.eq(executeId));
        }

        return findOne(getCompanyId(), EXECUTE_PDS_GROUP.EXECUTE_ID.eq(executeId));
    }
}
