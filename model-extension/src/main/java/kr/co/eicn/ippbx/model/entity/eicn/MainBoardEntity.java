package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoard;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardFile;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardPopup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardTarget;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MainBoardEntity extends MainBoard {
    private List<MainBoardFile> mainBoardFiles;
    private MainBoardPopup mainBoardPopup;
    private MainBoardTarget mainBoardTarget;
}
