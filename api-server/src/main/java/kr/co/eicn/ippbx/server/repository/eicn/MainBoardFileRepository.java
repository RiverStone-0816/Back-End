package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardFile;
import lombok.Getter;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardFile.MAIN_BOARD_FILE;

@Getter
@Repository
public class MainBoardFileRepository extends EicnBaseRepository<MainBoardFile, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile, Long>{
    protected final Logger logger = LoggerFactory.getLogger(MainBoardFileRepository.class);

    public MainBoardFileRepository() {
        super(MAIN_BOARD_FILE, MAIN_BOARD_FILE.FILE_ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile> findByMainBoardId(Long id){
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(MAIN_BOARD_FILE.MAIN_BOARD_ID.eq(id));

        return super.findAll(conditions);
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile findByFileAndBoardId(Long fileId, Long mainBoardId) {
        return dsl.select()
                .from(MAIN_BOARD_FILE)
                .where(MAIN_BOARD_FILE.FILE_ID.eq(fileId))
                .and(MAIN_BOARD_FILE.MAIN_BOARD_ID.eq(mainBoardId))
                .fetchOneInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile.class);
    }
}
