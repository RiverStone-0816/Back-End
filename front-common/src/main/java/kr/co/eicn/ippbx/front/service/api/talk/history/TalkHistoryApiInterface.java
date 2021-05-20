package kr.co.eicn.ippbx.front.service.api.talk.history;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.customdb.TalkMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkRoomResponse;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class TalkHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkHistoryApiInterface.class);
    private static final String subUrl = "/api/v1/admin/talk/history/";

    public Pagination<TalkRoomResponse> pagination(TalkRoomSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, TalkRoomResponse.class).getData();
    }

    public TalkRoomResponse get(Integer seq, String roomStatus) throws IOException, ResultFailException {
        return getData(subUrl + seq, Collections.singletonMap("roomStatus", roomStatus), TalkRoomResponse.class).getData();
    }

    public TalkRoomResponse get(String roomId) throws IOException, ResultFailException {
        return getData(subUrl + "/roomInfo", Collections.singletonMap("roomId", roomId), TalkRoomResponse.class).getData();
    }

    public List<TalkMsgResponse> messageHistory(String roomId) throws IOException, ResultFailException {
        return getList(subUrl + roomId + "/message", null, TalkMsgResponse.class).getData();
    }
}
