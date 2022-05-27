package kr.co.eicn.ippbx.front.service.api.wtalk.history;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.customdb.WtalkMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkRoomResponse;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class WtalkHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkHistoryApiInterface.class);
    private static final String subUrl = "/api/v1/admin/wtalk/history/";

    public Pagination<WtalkRoomResponse> pagination(TalkRoomSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, WtalkRoomResponse.class).getData();
    }

    public WtalkRoomResponse get(Integer seq, String roomStatus) throws IOException, ResultFailException {
        return getData(subUrl + seq, Collections.singletonMap("roomStatus", roomStatus), WtalkRoomResponse.class).getData();
    }

    public WtalkRoomResponse get(String roomId) throws IOException, ResultFailException {
        return getData(subUrl + "/roomInfo", Collections.singletonMap("roomId", roomId), WtalkRoomResponse.class).getData();
    }

    public List<WtalkMsgResponse> messageHistory(String roomId) throws IOException, ResultFailException {
        return getList(subUrl + roomId + "/message", null, WtalkMsgResponse.class).getData();
    }
}
