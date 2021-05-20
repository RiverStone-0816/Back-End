package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.NoticeXFile;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.NOTICE_X_FILE;

@Getter
@Repository
public class NoticeXFileRepository extends EicnBaseRepository<NoticeXFile, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.NoticeXFile, Long> {
    protected final Logger logger = LoggerFactory.getLogger(NoticeXFileRepository.class);

    public NoticeXFileRepository() {
        super(NOTICE_X_FILE,NOTICE_X_FILE.NOTICE,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.NoticeXFile.class);
    }

    public List<Long> findAllNoticeXFile(Long noticeId) {
        return dsl.select(NOTICE_X_FILE.FILE)
                .from(NOTICE_X_FILE)
                .where(compareCompanyId())
                .and(NOTICE_X_FILE.NOTICE.eq(noticeId))
                .fetch(NOTICE_X_FILE.FILE);
    }

    public void deleteOneFile(Long fileId) {
        dsl.deleteFrom(NOTICE_X_FILE)
                .where(compareCompanyId())
                .and(NOTICE_X_FILE.FILE.eq(fileId))
                .execute();
    }
}
