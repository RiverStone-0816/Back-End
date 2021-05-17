package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.NoticeFileEntity;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.NOTICE_FILE_ENTITY;

@Getter
@Repository
public class NoticeFileEntityRepository extends EicnBaseRepository<NoticeFileEntity, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.NoticeFileEntity, Long> {
    protected final Logger logger = LoggerFactory.getLogger(NoticeFileEntityRepository.class);

    public NoticeFileEntityRepository() {
        super(NOTICE_FILE_ENTITY,NOTICE_FILE_ENTITY.ID,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.NoticeFileEntity.class);
    }

    public Long nextFileId() {
        final NoticeFileEntity fileId = NOTICE_FILE_ENTITY.as("FILE_ID");
        return dsl.select(DSL.ifnull(DSL.max(fileId.ID), 0).add(1)).from(fileId).fetchOneInto(Long.class);
    }

    public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.NoticeFileEntity findOneFile(Long fileId) {
        return dsl.select(NOTICE_FILE_ENTITY.fields())
                .from(NOTICE_FILE_ENTITY)
                .where(compareCompanyId())
                .and(NOTICE_FILE_ENTITY.ID.eq(fileId))
                .fetchOneInto(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.NoticeFileEntity.class);
    }
}
