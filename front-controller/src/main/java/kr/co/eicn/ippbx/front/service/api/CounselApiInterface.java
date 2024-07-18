package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttMessage;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttText;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.form.TalkAutoEnableFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.model.dto.customdb.CustomMultichannelInfoResponse;
import kr.co.eicn.ippbx.model.dto.customdb.MainReceivePathResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkCurrentListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkCurrentMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TodoDataResponse;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.model.form.TodoListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.TodoReservationFormRequest;
import kr.co.eicn.ippbx.model.search.TodoListSearchRequest;
import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public List<WtalkCurrentListResponse> currentTalkList(TalkCurrentListSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "current-wtalk-list", search, WtalkCurrentListResponse.class).getData();
    }

    public WtalkCurrentMsgResponse currentTalkMsg(String roomId) throws IOException, ResultFailException {
        return getData(subUrl + "current-wtalk-msg" + "/" + roomId, null, WtalkCurrentMsgResponse.class).getData();
    }

    public void talkRemoveRoom(String roomId) throws IOException, ResultFailException {
        delete(subUrl + "wtalk-remove-room" + "/" + roomId);
    }

    public void updateTalkAutoEnable(String roomId, TalkAutoEnableFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "wtalk-auto-enable/" + roomId, form);
    }

    public List<CustomMultichannelInfoResponse> customInfoList(String channelData) throws IOException, ResultFailException {
        return getList(subUrl + "custom-info", Collections.singletonMap("channelData", channelData), CustomMultichannelInfoResponse.class).getData();
    }

    public List<SttText> sttText(String uniqueId) throws IOException, ResultFailException {
        return getList(subUrl + "stt-text/" + uniqueId, null, SttText.class).getData();
    }

    public List<SttMessage> sttChat(String uniqueId) throws IOException, ResultFailException {
        return getList(subUrl + "stt-chat/call-unique-id/" + uniqueId, null, SttMessage.class).getData();
    }

    @SneakyThrows
    public String uploadFile(FileForm form, String companyId, String channelType) {
        final String saveFileName = sendByMultipartFile(HttpMethod.POST, subUrl + "file", form, String.class, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));

        uploadWebchatImageToGateway(companyId, saveFileName, Objects.requireNonNull(TalkChannelType.of(channelType)));

        return saveFileName;
    }
}
