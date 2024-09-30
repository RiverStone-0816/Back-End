package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.LguCallbotInfo;
import kr.co.eicn.ippbx.model.dto.eicn.LguCallBotInfoResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.LguCallbotInfo.LGU_CALLBOT_INFO;

@Getter
@Repository
public class LguCallbotInfoRepository extends EicnBaseRepository<LguCallbotInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.LguCallbotInfo, String> {
    private final Logger logger = LoggerFactory.getLogger(LguCallbotInfoRepository.class);

    public LguCallbotInfoRepository() {
        super(LGU_CALLBOT_INFO, LGU_CALLBOT_INFO.TENANT_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.LguCallbotInfo.class);
    }

    public List<LguCallBotInfoResponse> getCallBotApiUrl() {
        return dsl.select(LGU_CALLBOT_INFO.TENANT_ID, LGU_CALLBOT_INFO.CALLBOT_KEY, LGU_CALLBOT_INFO.CONV_API_URL).from(LGU_CALLBOT_INFO)
                .where(compareCompanyId())
                .groupBy(LGU_CALLBOT_INFO.TENANT_ID, LGU_CALLBOT_INFO.CALLBOT_KEY, LGU_CALLBOT_INFO.CONV_API_URL)
                .fetchInto(LguCallBotInfoResponse.class);
    }
}
