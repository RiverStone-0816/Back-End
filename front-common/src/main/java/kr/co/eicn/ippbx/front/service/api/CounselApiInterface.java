package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.model.dto.customdb.CustomMultichannelInfoResponse;
import kr.co.eicn.ippbx.model.dto.customdb.MainReceivePathResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkCurrentListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkCurrentMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TodoDataResponse;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.model.form.TodoListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.TodoReservationFormRequest;
import kr.co.eicn.ippbx.model.search.TodoListSearchRequest;
import kr.co.eicn.ippbx.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class CounselApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CounselApiInterface.class);
    private static final String subUrl = "/api/v1/consultation/main/";

    public List<TodoDataResponse> getCurrentStatus() throws IOException, ResultFailException {
        return getList(subUrl + "to-do-monitor", null, TodoDataResponse.class).getData();
    }

    public List<TodoList> getTodoList(TodoListSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "to-do", search, TodoList.class).getData();
    }

    public void updateTodoStatus(TodoListUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "to-do", form);
    }

    public void reserveConsultation(TodoReservationFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "consultation-reservation", form);
    }

    public List<TodoList> getRecentReservations() throws IOException, ResultFailException {
        return getList(subUrl + "recent-consultation-reservation", null, TodoList.class).getData();
    }

    public List<MainReceivePathResponse> callingPath(String iniNum, String secondNum) throws IOException, ResultFailException {
        return getList(subUrl + "calling-path", UrlUtils.makeParamMap("iniNum", iniNum, "secondNum", secondNum == null ? "" : secondNum), MainReceivePathResponse.class).getData();
    }

    public List<TalkCurrentListResponse> currentTalkList(TalkCurrentListSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "current-talk-list", search, TalkCurrentListResponse.class).getData();
    }

    public TalkCurrentMsgResponse currentTalkMsg(String roomId) throws IOException, ResultFailException {
        return getData(subUrl + "current-talk-msg" + "/" + roomId, null, TalkCurrentMsgResponse.class).getData();
    }

    public void talkRemoveRoom(String roomId) throws IOException, ResultFailException {
        delete(subUrl + "talk-remove-room" + "/" + roomId);
    }

    public List<CustomMultichannelInfoResponse> customInfoList(String channelData) throws IOException, ResultFailException {
        return getList(subUrl + "custom-info", Collections.singletonMap("channelData", channelData), CustomMultichannelInfoResponse.class).getData();
    }
}
