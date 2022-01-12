package kr.co.eicn.ippbx.front.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.exception.UnauthorizedException;
import kr.co.eicn.ippbx.util.ResultFailException;
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
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @SneakyThrows
    public void uploadFile(FileUploadForm o) {
        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        if (o != null) {
            final Map<String, Object> params = objectMapper.convertValue(o, new ObjectMapper().getTypeFactory().constructParametricType(Map.class, String.class, Object.class));
            params.forEach(parts::add);
        }
        parts.add("up_filename", new FileResource(Objects.requireNonNull(o).getFilePath(), o.getOriginalName(), false));

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final String accessToken = getAccessToken();
        if (StringUtils.isNotEmpty(accessToken))
            headers.add("Authorization", "Bearer " + accessToken);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

        try {
            restTemplate.exchange(o.getBasic_url() + "/talk/fileupload", HttpMethod.POST, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            if (Objects.equals(e.getStatusCode(), HttpStatus.UNAUTHORIZED))
                throw new UnauthorizedException(e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), Charset.defaultCharset());
            throw e;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class FileUploadForm extends BaseForm {
        @NotNull("파일경로")
        private String filePath;
        @NotNull("파일이름")
        private String originalName;

        private String my_userid;
        private String my_username;
        private String company_id;
        private String basic_url;
        private String web_url;
        private String room_id;
        @NotNull("channel_type")
        private String channel_type;
        @NotNull("sender_key")
        private String sender_key;
        @NotNull("user_key")
        private String user_key;
    }
}
