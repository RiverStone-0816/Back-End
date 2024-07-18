package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonSttMessage;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttMessage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
public class SttMessageRepository extends CustomDBBaseRepository<CommonSttMessage, SttMessage, Integer>{
    private final Logger logger = LoggerFactory.getLogger(SttMessageRepository.class);
    private CommonSttMessage TABLE;

    public SttMessageRepository(String companyId) {
        super(new CommonSttMessage(companyId), new CommonSttMessage(companyId).SEQ, SttMessage.class);
        this.TABLE = new CommonSttMessage(companyId);
    }

    public List<SttMessage> findAll(String uniqueId) {
        return super.findAll(TABLE.CALL_UNIQUEID.eq(uniqueId));
    }
}
