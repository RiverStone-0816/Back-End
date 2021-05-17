package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.util.JsonResult;
import kr.co.eicn.ippbx.server.model.dto.configdb.ChattRoomResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.server.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.server.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.server.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.server.model.search.ChattingRoomSearchRequest;
import kr.co.eicn.ippbx.server.model.search.ChattingSearchRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
