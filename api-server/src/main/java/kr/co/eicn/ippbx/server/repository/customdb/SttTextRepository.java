package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonSttText;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttText;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
public class SttTextRepository extends CustomDBBaseRepository<CommonSttText, SttText, Integer>{
    private final Logger logger = LoggerFactory.getLogger(SttTextRepository.class);
    private CommonSttText TABLE;

    public SttTextRepository(String companyId) {
        super(new CommonSttText(companyId), new CommonSttText(companyId).SEQ, SttText.class);
        this.TABLE = new CommonSttText(companyId);
    }

    public List<SttText> findAll(String uniqueId) {
        return super.findAll(TABLE.CALL_UNIQUEID.eq(uniqueId));
    }

    public void updateRemind(String messageId) {
        final String remindYn = dsl.select(TABLE.MESSAGE_ID)
                .from(TABLE)
                .where(TABLE.MESSAGE_ID.eq(messageId))
                .fetchOneInto(String.class);
        dsl.update(TABLE)
                .set(TABLE.REMIND_YN, remindYn.equals("Y") ? "N" : "Y")
                .where(TABLE.MESSAGE_ID.eq(messageId))
                .execute();
    }

    public String adminMonit(String userId) {
        return dsl.select(TABLE.CALL_UNIQUEID)
                .from(TABLE)
                .where(TABLE.IPCC_USERID.eq(userId))
                .orderBy(TABLE.INSERT_TIME.desc())
                .limit(1)
                .fetchOneInto(String.class);
    }
}
