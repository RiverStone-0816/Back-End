package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardFile;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardFile.MAIN_BOARD_FILE;

@Getter
@Repository
public class MainBoardFileRepository extends EicnBaseRepository<MainBoardFile, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile, Long>{
    protected final Logger logger = LoggerFactory.getLogger(MainBoardFileRepository.class);

    public MainBoardFileRepository() {
        super(MAIN_BOARD_FILE, MAIN_BOARD_FILE.FILE_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile.class);
    }
}
