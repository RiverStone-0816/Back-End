package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ManualFileEntity;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.MANUAL_FILE_ENTITY;

@Getter
@Repository
public class ManualFileEntityRepository extends EicnBaseRepository<ManualFileEntity, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ManualFileEntity, Long> {
    protected final Logger logger = LoggerFactory.getLogger(ManualFileEntityRepository.class);

    public ManualFileEntityRepository() {
        super(MANUAL_FILE_ENTITY,MANUAL_FILE_ENTITY.ID,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ManualFileEntity.class);
    }

    public Long nextFileId() {
        final ManualFileEntity fileId = MANUAL_FILE_ENTITY.as("FILE_ID");
        return dsl.select(DSL.ifnull(DSL.max(fileId.ID), 0).add(1)).from(fileId).fetchOneInto(Long.class);
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ManualFileEntity findOneFile(Long fileId) {
        return dsl.select(MANUAL_FILE_ENTITY.fields())
                .from(MANUAL_FILE_ENTITY)
                .where(compareCompanyId())
                .and(MANUAL_FILE_ENTITY.ID.eq(fileId))
                .fetchOneInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ManualFileEntity.class);
    }
}
