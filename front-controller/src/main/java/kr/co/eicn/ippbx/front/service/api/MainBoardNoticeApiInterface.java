package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.model.entity.eicn.MainBoardEntity;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MainBoardNoticeApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MainBoardNoticeApiInterface.class);
    private final String subUrl = "/api/main-board-notice/";

    public List<MainBoardEntity> after() throws IOException, ResultFailException {
        return getList(subUrl + "after", null, MainBoardEntity.class).getData();
    }

    public List<MainBoardEntity> before() throws IOException, ResultFailException {
        return getList(subUrl + "before", null, MainBoardEntity.class).getData();
    }
}
