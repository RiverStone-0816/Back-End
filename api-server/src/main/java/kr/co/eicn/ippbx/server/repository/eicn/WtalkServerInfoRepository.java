package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServerInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServerInfo.WTALK_SERVER_INFO;

@Getter
@Repository
public class WtalkServerInfoRepository extends EicnBaseRepository<WtalkServerInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServerInfo, String>{
    protected final Logger logger = LoggerFactory.getLogger(WtalkServerInfoRepository.class);

    WtalkServerInfoRepository() {
        super(WTALK_SERVER_INFO, WTALK_SERVER_INFO.TALK_HOST, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServerInfo.class);
    }

}
