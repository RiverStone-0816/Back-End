package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.history;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonWtalkRoom;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.TalkRoom;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.customdb.TalkMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkRoomResponse;
import kr.co.eicn.ippbx.model.entity.customdb.TalkRoomEntity;
import kr.co.eicn.ippbx.model.enums.RoomStatus;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CurrentWtalkRoomRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.server.service.WtalkMsgService;
import kr.co.eicn.ippbx.server.service.WtalkRoomService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentTalkRoom.CURRENT_TALK_ROOM;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.replace;

/**
 * 상담톡관리 > 상담톡이력 > 상담톡이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkRoomApiController extends ApiBaseController {
    private final CurrentWtalkRoomRepository currentTalkRoomRepository;
    private final WtalkRoomService talkRoomService;
    private final PersonListRepository personListRepository;
    private final WtalkMsgService talkMsgService;
    private final StorageService fileSystemStorageService;
    @Value("${file.path.talk}")
    private String location;


    /**
     * 목록 조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<TalkRoomResponse>>> pagination(TalkRoomSearchRequest search) {
        Pagination<TalkRoomEntity> pagination;
        List<PersonList> personList = personListRepository.findAll();

        if (search.getStartDate() != null && search.getEndDate() != null)
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");
        if (Objects.isNull(search.getSort()))
            search.setSorts(TalkRoomSearchRequest.Sorts.START_TIME);
        if (StringUtils.isEmpty(search.getSequence()))
            search.setSequence("desc");

        if (RoomStatus.PROCESS_OR_STOP.getCode().equals(search.getRoomStatus())) {
            pagination = currentTalkRoomRepository.pagination(search);
        } else if (RoomStatus.DOWN.getCode().equals(search.getRoomStatus())) {
            pagination = talkRoomService.getRepository().pagination(search);
        } else
            throw new IllegalArgumentException(message.getText("messages.validator.invalid", "방상태 (진행.중지/내려짐)"));

        List<TalkRoomResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    TalkRoomResponse talkRoomResponse = convertDto(e, TalkRoomResponse.class);

                    talkRoomResponse.setIdName(personList.stream().filter(person -> person.getId().equals(e.getUserid())).map(PersonList::getIdName).findFirst().orElse(""));
                    talkRoomResponse.setChannelType(TalkChannelType.of(e.getChannelType()));

                    return talkRoomResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<TalkRoomResponse>> get(@PathVariable Integer seq, @RequestParam String roomStatus) {
        if (StringUtils.isEmpty(roomStatus))
            throw new IllegalArgumentException(message.getText("messages.validator.blank", "방상태"));

        TalkRoomEntity roomEntity;
        if (RoomStatus.PROCESS.getCode().equals(roomStatus)
                || RoomStatus.STOP.getCode().equals(roomStatus)
                || RoomStatus.PROCESS_OR_STOP.getCode().equals(roomStatus)
                || RoomStatus.BOT_PLAY.getCode().equals(roomStatus)
                || RoomStatus.SERVICE_BY_GROUP_CONNECT.getCode().equals(roomStatus))
            roomEntity = currentTalkRoomRepository.findOneIfNullThrow(seq);
        else if (RoomStatus.DOWN.getCode().equals(roomStatus))
            roomEntity = talkRoomService.getRepository().findOneIfNullThrow(seq);
        else
            throw new IllegalArgumentException(message.getText("\"messages.validator.invalid", "방상태"));

        TalkRoomResponse talkRoomResponse = convertDto(roomEntity, TalkRoomResponse.class);
        if (roomEntity.getUserid() != null && !roomEntity.getUserid().equals(""))
            talkRoomResponse.setIdName(personListRepository.findOne(roomEntity.getUserid()).getIdName());
        talkRoomResponse.setChannelType(TalkChannelType.of(roomEntity.getChannelType()));

        return ResponseEntity.ok(data(talkRoomResponse));
    }

    @GetMapping("roomInfo")
    public ResponseEntity<JsonResult<TalkRoomResponse>> get(@RequestParam String roomId) {

        TalkRoomEntity roomEntity;
        roomEntity = currentTalkRoomRepository.findOne(CURRENT_TALK_ROOM.ROOM_ID.eq(roomId));
        if(Objects.isNull(roomEntity))
            roomEntity = talkRoomService.getRepository().findOne(new CommonWtalkRoom(g.getUser().getCompanyId()).ROOM_ID.eq(roomId));

        TalkRoomResponse talkRoomResponse = convertDto(roomEntity, TalkRoomResponse.class);
        talkRoomResponse.setIdName(personListRepository.findOne(roomEntity.getUserid()).getIdName());
        talkRoomResponse.setChannelType(TalkChannelType.of(roomEntity.getChannelType()));
        return ResponseEntity.ok(data(talkRoomResponse));
    }

    /**
     * 대화내용 보기
     */
    @GetMapping("{roomId}/message")
    public ResponseEntity<JsonResult<List<TalkMsgResponse>>> messageHistory(@PathVariable String roomId) {
        List<PersonList> personList = personListRepository.findAll();

        return ResponseEntity.ok(data(
                talkMsgService.getRepository().findAll(roomId).stream()
                        .map(msg -> {
                            TalkMsgResponse response = convertDto(msg, TalkMsgResponse.class);

                            response.setIdName(personList.stream().filter(person -> person.getId().equals(msg.getUserid())).map(PersonList::getIdName).findFirst().orElse(""));

                            return response;
                        }).collect(Collectors.toList())
                )
        );
    }

    @GetMapping(value = "resource", params = {"token"})
    public ResponseEntity<Resource> resource(@RequestParam("path") String filePath, @RequestParam("fileName") String fileName) {
        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(replace(location, "{0}", g.getUser().getCompanyId()), filePath), fileName);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
