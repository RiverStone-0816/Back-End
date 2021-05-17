package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.FileEntity;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.FILE_ENTITY;

@Getter
@Repository
public class TaskFileEntityRepository extends EicnBaseRepository<FileEntity, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity, Long> {
    protected final Logger logger = LoggerFactory.getLogger(TaskFileEntityRepository.class);

    public TaskFileEntityRepository() {
        super(FILE_ENTITY,FILE_ENTITY.ID,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity.class);
    }

    public Long nextFileId() {
        final FileEntity fileId = FILE_ENTITY.as("FILE_ID");
        return dsl.select(DSL.ifnull(DSL.max(fileId.ID), 0).add(1)).from(fileId).fetchOneInto(Long.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity> getOriginalFileName(String fileName) {
        return dsl.select(FILE_ENTITY.ORIGINAL_NAME)
                .from(FILE_ENTITY)
                .where(compareCompanyId())
                .and(FILE_ENTITY.ORIGINAL_NAME.eq(fileName))
                .fetchInto(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity.class);
    }

    public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity findOneFile(Long fileId) {
        return dsl.select(FILE_ENTITY.fields())
                .from(FILE_ENTITY)
                .where(compareCompanyId())
                .and(FILE_ENTITY.ID.eq(fileId))
                .fetchOneInto(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity.class);
    }
}
