package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.ChattingApiInterface;
import kr.co.eicn.ippbx.model.dto.configdb.ChattRoomResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ScoreMonitorResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingRoomSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@RestController
@RequestMapping(value = "api/chatt", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChattingApiController extends BaseController {

    private final ChattingApiInterface apiInterface;

    @SneakyThrows
    @GetMapping("")
    public List<OrganizationMetaChatt> getOrganizationMetaChatt(ChattingMemberSearchRequest search) {
        return apiInterface.getOrganizationMetaChatt(search);
    }

    @SneakyThrows
    @GetMapping("bookmark-list")
    public List<PersonDetailResponse> getBookmarkMembers(ChattingMemberSearchRequest search) {
        return apiInterface.getBookmarkMembers(search);
    }

    @SneakyThrows
    @GetMapping("chatt-room")
    public List<ChattRoomResponse> getChattingRoomList(ChattingRoomSearchRequest search) {
        return apiInterface.getChattingRoomList(search);
    }

    @SneakyThrows
    @GetMapping("{roomId}")
    public ChattRoomEntity getChattingRoom(@PathVariable String roomId) {
        return apiInterface.getChattingRoom(roomId);
    }

    @SneakyThrows
    @GetMapping("{roomId}/chatting")
    public ChattRoomEntity getChattingRoom(@PathVariable String roomId, ChattingSearchRequest search) {
        return apiInterface.getChattingRoom(roomId, search);
    }

    @SneakyThrows
    @PostMapping("")
    public String insertChattingRoomIfNotExists(@Valid @RequestBody ChattingMemberFormRequest form, BindingResult bindingResult) {
        return apiInterface.insertChattingRoomIfNotExists(form);
    }

    @SneakyThrows
    @PutMapping("{roomId}/chatt-member")
    public void addChattingMembers(@Valid @RequestBody ChattingMemberFormRequest form, BindingResult bindingResult, @PathVariable String roomId) {
        apiInterface.addChattingMembers(roomId, form);
    }

    @SneakyThrows
    @PutMapping("{roomId}/room-name")
    public void updateChattingRoomName(@PathVariable String roomId, @RequestParam String newRoomName) {
        apiInterface.updateChattingRoomName(roomId, newRoomName);
    }

    @SneakyThrows
    @PutMapping("bookmark")
    public void updateBookMark(@Valid @RequestBody ChattingMemberFormRequest form, BindingResult bindingResult) {
        apiInterface.updateBookMark(form);
    }

    @SneakyThrows
    @DeleteMapping("{roomId}")
    public void deleteChattingRoom(@PathVariable String roomId) {
        apiInterface.deleteChattingRoom(roomId);
    }

    @SneakyThrows
    @GetMapping("bookmark")
    public List<PersonDetailResponse> getOrganizationMetaBookmark() {
        return apiInterface.getOrganizationMetaBookmark();
    }

    @SneakyThrows
    @GetMapping("user-score-moniter")
    public List<ScoreMonitorResponse> userScoreMoniter() {
        return apiInterface.userScoreMoniter();
    }

    @SneakyThrows
    @PostMapping("{roomId}/upload-file")
    public void uploadFile(@PathVariable String roomId, @Valid @RequestBody ChattingApiInterface.FileUploadForm form, BindingResult bindingResult, @Value("${eicn.apiserver}") String apiServer, @Value("${eicn.messenger.socket.id}") String messengerSocketId) {
        form.setMy_userid(g.getUser().getId());
        form.setMy_username(g.getUser().getIdName());
        form.setCompany_id(g.getUser().getCompanyId());
        form.setBasic_url(g.getSocketList().get(messengerSocketId));
        form.setWeb_url(apiServer);
        form.setRoom_id(roomId);
        apiInterface.uploadFile(form);
    }
}
