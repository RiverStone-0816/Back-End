package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonSttCdr;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttCdr;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class SttCdrRepository extends CustomDBBaseRepository<CommonSttCdr, SttCdr, String> {
    private final Logger logger = LoggerFactory.getLogger(SttCdrRepository.class);
    private CommonSttCdr TABLE;

    public SttCdrRepository(String companyId) {
        super(new CommonSttCdr(companyId), new CommonSttCdr(companyId).CALL_UNIQUEID, SttCdr.class);
        this.TABLE = new CommonSttCdr(companyId);
    }

    public SttCdr findByCallUniqueId(String callUniqueId) {
        return super.findOne(TABLE.CALL_UNIQUEID.eq(callUniqueId));
    }
}
