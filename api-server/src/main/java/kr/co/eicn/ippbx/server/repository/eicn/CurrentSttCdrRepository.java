package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentSttCdr;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentSttCdr.CURRENT_STT_CDR;

@Getter
@Repository
public class CurrentSttCdrRepository extends EicnBaseRepository<CurrentSttCdr, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentSttCdr, String>{
    protected final Logger logger = LoggerFactory.getLogger(CurrentSttCdrRepository.class);

    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public CurrentSttCdrRepository(CacheService cacheService, PBXServerInterface pbxServerInterface) {
        super(CURRENT_STT_CDR, CURRENT_STT_CDR.CALL_UNIQUEID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentSttCdr.class);

        this.cacheService = cacheService;
        this.pbxServerInterface = pbxServerInterface;
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CurrentSttCdr findByCallUniqueId(String callUniqueId) {
        return super.findOne(CURRENT_STT_CDR.CALL_UNIQUEID.eq(callUniqueId));
    }
}
