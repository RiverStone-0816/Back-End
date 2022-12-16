package kr.co.eicn.ippbx.front.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.exception.UnauthorizedException;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.model.dto.configdb.ChattRoomResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ScoreMonitorResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingRoomSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Service
public class ChattingApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/chatt/";

    @SneakyThrows
    public List<OrganizationMetaChatt> getOrganizationMetaChatt(ChattingMemberSearchRequest search) {
        return getList(subUrl, search, OrganizationMetaChatt.class).getData();
    }

    @SneakyThrows
    public List<PersonDetailResponse> getBookmarkMembers(ChattingMemberSearchRequest search) {
        return getList(subUrl + "bookmark-list", search, PersonDetailResponse.class).getData();
    }

    @SneakyThrows
    public List<ChattRoomResponse> getChattingRoomList(ChattingRoomSearchRequest search) {
        return getList(subUrl + "chatt-room", search, ChattRoomResponse.class).getData();
    }

    @SneakyThrows
    public ChattRoomEntity getChattingRoom(String roomId) {
        return getData(subUrl + roomId + "", null, ChattRoomEntity.class).getData();
    }

    @SneakyThrows
    public ChattRoomEntity getChattingRoom(String roomId, ChattingSearchRequest search) {
        return getData(subUrl + roomId + "/chatting", search, ChattRoomEntity.class).getData();
    }

    @SneakyThrows
    public String insertChattingRoomIfNotExists(ChattingMemberFormRequest form) {
        return getData(HttpMethod.POST, subUrl, form, String.class, false).getData();
    }

    @SneakyThrows
    public void addChattingMembers(String roomId, ChattingMemberFormRequest form) {
        put(subUrl + roomId + "/chatt-member", form);
    }

    @SneakyThrows
    public void updateChattingRoomName(String roomId, String newRoomName) {
        final JsonResult<?> result = call(subUrl + roomId + "/room-name", Collections.singletonMap("newRoomName", newRoomName), JsonResult.class, HttpMethod.PUT, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    @SneakyThrows
    public void updateBookMark(ChattingMemberFormRequest form) {
        put(subUrl + "bookmark", form);
    }

    @SneakyThrows
    public void deleteChattingRoom(String roomId) {
        delete(subUrl + roomId);
    }

    @SneakyThrows
    public List<PersonDetailResponse> getOrganizationMetaBookmark() {
        return getList(subUrl + "bookmark", null, PersonDetailResponse.class).getData();
    }

    @SneakyThrows
    public List<ScoreMonitorResponse> userScoreMoniter(){
        return getList(subUrl + "user-score-moniter", null, ScoreMonitorResponse.class).getData();
    }

    @SneakyThrows
    public void uploadFile(FileUploadForm o, MultipartFile f) {
        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        if (o != null) {
            final Map<String, Object> params = objectMapper.convertValue(o, new ObjectMapper().getTypeFactory().constructParametricType(Map.class, String.class, Object.class));
            params.forEach(parts::add);
        }
        parts.add("up_filename", f.getResource());

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

        try {
            restTemplate.exchange(o.getBasic_url() + "/fileupload", HttpMethod.POST, requestEntity, String.class);
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
    }

}
