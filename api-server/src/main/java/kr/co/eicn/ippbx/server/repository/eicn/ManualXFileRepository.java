package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ManualXFile;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.MANUAL_X_FILE;

@Getter
@Repository
public class ManualXFileRepository extends EicnBaseRepository<ManualXFile, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ManualXFile, Long> {
    protected final Logger logger = LoggerFactory.getLogger(ManualXFileRepository.class);

    public ManualXFileRepository() {
        super(MANUAL_X_FILE,MANUAL_X_FILE.MANUAL,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ManualXFile.class);
    }

    public List<Long> findAllManualXFile(Long manualId) {
        return dsl.select(MANUAL_X_FILE.FILE)
                .from(MANUAL_X_FILE)
                .where(compareCompanyId())
                .and(MANUAL_X_FILE.MANUAL.eq(manualId))
                .fetch( MANUAL_X_FILE.FILE);
    }

    public void deleteOneFile(Long fileId) {
        dsl.deleteFrom(MANUAL_X_FILE)
                .where(compareCompanyId())
                .and(MANUAL_X_FILE.FILE.eq(fileId))
                .execute();
    }
}
