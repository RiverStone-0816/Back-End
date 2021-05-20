package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentMemberStatus;
import kr.co.eicn.ippbx.model.dto.eicn.DashCurrentResultCallResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CenterMemberStatusCountEntity;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.CURRENT_MEMBER_STATUS;
import static org.jooq.impl.DSL.*;

@Getter
@Repository
public class CurrentMemberStatusRepository extends EicnBaseRepository<CurrentMemberStatus, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentMemberStatus, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CurrentMemberStatusRepository.class);

    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public CurrentMemberStatusRepository(CacheService cacheService, PBXServerInterface pbxServerInterface) {
        super(CURRENT_MEMBER_STATUS, CURRENT_MEMBER_STATUS.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentMemberStatus.class);
        this.pbxServerInterface = pbxServerInterface;
        this.cacheService = cacheService;
    }

    public DashCurrentResultCallResponse getCurrentResultCall() {

        DashCurrentResultCallResponse response = getCurrentResult(dsl);
        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = getPbxServerInterface().using(e.getServer().getIp())) {
                        DashCurrentResultCallResponse res = getCurrentResult(pbxDsl);

                        response.setInCallingCount(response.getInCallingCount() + res.getInCallingCount());
                        response.setOutCallingCount(response.getOutCallingCount() + res.getOutCallingCount());

                    }
                });

        return response;
    }

    private DashCurrentResultCallResponse getCurrentResult(DSLContext dslContext) {
        return dslContext.select(DSL.count(DSL.when(CURRENT_MEMBER_STATUS.IN_OUT.eq("I"), 1)).as("in_calling_count"))
                .select(DSL.count(DSL.when(CURRENT_MEMBER_STATUS.IN_OUT.eq("O"), 1)).as("out_calling_count"))
                .from(CURRENT_MEMBER_STATUS)
                .where(compareCompanyId())
                .and(CURRENT_MEMBER_STATUS.STATUS.eq(CURRENT_MEMBER_STATUS.NEXT_STATUS))
                .and(CURRENT_MEMBER_STATUS.STATUS.eq("1"))
                .fetchOneInto(DashCurrentResultCallResponse.class);
    }

    public MonitorQueuePersonStatResponse getCurrentMemberStatus(String userId) {

        MonitorQueuePersonStatResponse response = getCurrentStatus(dsl, userId);

        if (response != null) {
            cacheService.pbxServerList(getCompanyId())
                    .forEach(e -> {
                        try (DSLContext pbxDsl = getPbxServerInterface().using(e.getHost())) {
                            MonitorQueuePersonStatResponse res = getCurrentStatus(pbxDsl, userId);

                            response.setInOut(res.getInOut() == null ? "N" : res.getInOut());
                        }
                    });
        }

        return response;
    }

    public CenterMemberStatusCountEntity getCenterStatusCount() {
        CenterMemberStatusCountEntity entity = getCenterStatusCount(dsl);
        cacheService.pbxServerList(getCompanyId())
                .forEach(e -> {
                    try (DSLContext pbxDsl = pbxServerInterface.using(e.getHost())) {
                        CenterMemberStatusCountEntity pbxEntity = getCenterStatusCount(pbxDsl);
                        entity.setInCallingCount(pbxEntity.getInCallingCount());
                        entity.setOutCallingCount(pbxEntity.getOutCallingCount());
                    }
                });

        return entity;
    }

    private CenterMemberStatusCountEntity getCenterStatusCount(DSLContext dslContext) {
        return dslContext.select(
                count(when(CURRENT_MEMBER_STATUS.IN_OUT.eq("I").and(CURRENT_MEMBER_STATUS.STATUS.eq("1")), 1)).as("in_calling_count"),
                count(when(CURRENT_MEMBER_STATUS.IN_OUT.eq("O").and(CURRENT_MEMBER_STATUS.STATUS.eq("1")), 1)).as("out_calling_count"))
                .from(CURRENT_MEMBER_STATUS)
                .where(compareCompanyId())
                .and(CURRENT_MEMBER_STATUS.STATUS.eq(CURRENT_MEMBER_STATUS.NEXT_STATUS))
                .fetchOneInto(CenterMemberStatusCountEntity.class);
    }

    private MonitorQueuePersonStatResponse getCurrentStatus(DSLContext dslContext, String userId) {
        LocalDate currentDate = LocalDate.now();
        return dslContext.select(CURRENT_MEMBER_STATUS.STATUS.as("status_number"))
                .select(CURRENT_MEMBER_STATUS.IN_OUT.as("in_out"))
                .from(CURRENT_MEMBER_STATUS)
                .where(compareCompanyId())
                .and(CURRENT_MEMBER_STATUS.START_DATE.ge(timestamp(currentDate + " 00:00:00")))
                .and(CURRENT_MEMBER_STATUS.END_DATE.le(timestamp(currentDate + " 23:59:59")))
                .and(CURRENT_MEMBER_STATUS.STATUS.eq(CURRENT_MEMBER_STATUS.NEXT_STATUS))
                .and(CURRENT_MEMBER_STATUS.PHONENAME.eq(userId))
                .fetchOneInto(MonitorQueuePersonStatResponse.class);
    }
}
