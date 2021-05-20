package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.HistoryUploadInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.HistoryUploadInfo.HISTORY_UPLOAD_INFO;

@Getter
@Repository
public class HistoryUploadInfoRepository extends EicnBaseRepository<HistoryUploadInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryUploadInfo, String> {
    private final Logger logger = LoggerFactory.getLogger(HistoryUploadInfoRepository.class);

    public HistoryUploadInfoRepository() {
        super(HISTORY_UPLOAD_INFO, HISTORY_UPLOAD_INFO.UPLOAD_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryUploadInfo.class);
    }
}
