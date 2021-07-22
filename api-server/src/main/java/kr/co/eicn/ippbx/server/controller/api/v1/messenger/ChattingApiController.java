package kr.co.eicn.ippbx.server.controller.api.v1.messenger;

import kr.co.eicn.ippbx.model.dto.eicn.ScoreMonitorResponse;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.customdb.ChattRoomResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingRoomSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.*;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.replaceEach;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/chatt", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChattingApiController extends ApiBaseController {

    private final ChattRoomService roomService;
    private final ChattMemberService memberService;
    private final ChattBookmarkService bookmarkService;
    private final OrganizationService organizationService;
    private final PersonListRepository personListRepository;
    private final FileSystemStorageService fileSystemStorageService;
    private final StatUserInboundService statUserInboundService;
    private final StatUserOutboundService statUserOutboundService;
    private final TalkStatisticsService talkStatisticsService;

    @Value("${file.path.chatt}")
    private String savePath;

    /**
     * 조직도 한번에 보내기.
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<List<OrganizationMetaChatt>>> getOrganizationMetaChatt(ChattingMemberSearchRequest search) {
        return ResponseEntity.ok(data(organizationService.getOrganizationMetaChatt(search)));
    }

    /**
     * 즐겨찾기 멤버 리스트
     * chatt_bookmark 테이블의 memberId와 일치하는 사용자만 나옴. ( person_list )
     */
    @GetMapping("bookmark-list")
    public ResponseEntity<JsonResult<List<PersonDetailResponse>>> getBookmarkMembers(ChattingMemberSearchRequest search) {
        return ResponseEntity.ok(data(bookmarkService.getBookmarks(search)));
    }

    /**
     * 본인이 속한 채팅방 전체 목록
     * ( chatt_room_member의 joinStatus가 N일 경우 제외 )
     */
    @GetMapping("chatt-room")
    public ResponseEntity<JsonResult<List<ChattRoomResponse>>> getChattingRoomList(ChattingRoomSearchRequest search) {
        return ResponseEntity.ok(data(roomService.getChattingRoomList(search)));
    }

    /**
     * 특정 채팅방 (연산이 많아 분리함. 방 정보만 전달)
     */
    @GetMapping("{roomId}")
    public ResponseEntity<JsonResult<ChattRoomEntity>> getChattingRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(data(roomService.findOneChattingRoom(roomId)));
    }

    /**
     * 특정 채팅방
     */
    @GetMapping("{roomId}/chatting")
    public ResponseEntity<JsonResult<ChattRoomEntity>> getChattingRoom(@PathVariable String roomId, ChattingSearchRequest search) {
        return ResponseEntity.ok(data(roomService.getChattingRoom(roomId, search)));
    }

    /**
     * 채팅하고자 하는 멤버들의 채팅 이력이 없다면 새로운 채팅방 생성.
     */
    @PostMapping("")
    public ResponseEntity<JsonResult<String>> insertChattingRoomIfNotExists(@Valid @RequestBody ChattingMemberFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final String roomId = roomService.getChattingRoomIfExists(form.getMemberList());
        return ResponseEntity.ok(data(roomId != null ? roomId : roomService.getRepository().insertChattingRoom(form)));
    }

    @PutMapping("{roomId}/chatt-member")
    public ResponseEntity<JsonResult<String>> addChattingMembers(@Valid @RequestBody ChattingMemberFormRequest form, BindingResult bindingResult,
                                                                 @PathVariable String roomId) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.ok(data(memberService.getRepository().addChattingMembers(roomId, form)));
    }

    @PutMapping("{roomId}/room-name")
    public ResponseEntity<JsonResult<Void>> updateChattingRoomName(@PathVariable String roomId, @RequestParam String newRoomName) {
        roomService.getRepository().updateChattingRoomName(roomId, newRoomName);
        return ResponseEntity.ok(create());
    }

    @PutMapping("bookmark")
    public ResponseEntity<JsonResult<Void>> updateBookMark(@Valid @RequestBody ChattingMemberFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        bookmarkService.getRepository().updateBookMark(form);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{roomId}")
    public ResponseEntity<JsonResult<Void>> deleteChattingRoom(@PathVariable String roomId) {
        roomService.getRepository().deleteChattingRoom(roomId);
        return ResponseEntity.ok(create());
    }

    /**
     * 즐겨찾기 조직도
     */
    @GetMapping("bookmark")
    public ResponseEntity<JsonResult<List<PersonDetailResponse>>> getOrganizationMetaBookmark() {
        return ResponseEntity.ok(data(personListRepository.findAllByChatting(new ChattingMemberSearchRequest()).stream()
                .map(e -> convertDto(e, PersonDetailResponse.class)).collect(Collectors.toList())));
    }

    /**
     * 파일 다운로드
     */
    @GetMapping(value = "history/resource", params = {"token"})
    public ResponseEntity<Resource> specificFileResource(@RequestParam String path, @RequestParam String fileName, HttpServletRequest request) {
        final String[] dirInfo = path.split("/");

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(replaceEach(savePath, new String[] {"{0}", "{1}", "{2}"}, new String[] {g.getUser().getCompanyId(), dirInfo[0], dirInfo[1]})), fileName);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }

    @GetMapping("user-score-moniter")
    public ResponseEntity<JsonResult<List<ScoreMonitorResponse>>> userScoreMoniter(){
        final List<String> person = personListRepository.personAllId();
        final Map<String, Object> inbound = statUserInboundService.getRepository().findChatUserInboundMonitor(person);
        final Map<String, Object> outbound = statUserOutboundService.getRepository().findChatUserOutboundMonitor(person);
        final Map<String, Object> talk = talkStatisticsService.getRepository().findChatUserTalkMonitor(person);

        List<ScoreMonitorResponse> list = person.stream().map(e -> {
            ScoreMonitorResponse response = new ScoreMonitorResponse();
            response.setUserId(e);
            response.setInCnt(Objects.nonNull(inbound.get(e)) ? (Integer) inbound.get(e) : 0);
            response.setOutCnt(Objects.nonNull(outbound.get(e)) ? (Integer) outbound.get(e) : 0);
            response.setTalkCnt(Objects.nonNull(talk.get(e)) ? (Integer) talk.get(e) : 0);
            response.setTotalCnt(response.getInCnt()+response.getOutCnt());
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }
}
