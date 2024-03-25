package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.history;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonWtalkRoom;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.customdb.WtalkMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkRoomResponse;
import kr.co.eicn.ippbx.model.entity.customdb.WtalkRoomEntity;
import kr.co.eicn.ippbx.model.enums.RoomStatus;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.CurrentWtalkRoomRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.server.service.WtalkMsgService;
import kr.co.eicn.ippbx.server.service.WtalkRoomService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentWtalkRoom.CURRENT_WTALK_ROOM;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.replace;

/**
 * 상담톡관리 > 상담톡이력 > 상담톡이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkRoomApiController extends ApiBaseController {
    private final CurrentWtalkRoomRepository currentWtalkRoomRepository;
    private final WtalkRoomService wtalkRoomService;
    private final PersonListRepository personListRepository;
    private final WtalkMsgService wtalkMsgService;
    private final StorageService fileSystemStorageService;
    @Value("${file.path.talk}")
    private String location;

    /**
     * 목록 조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<WtalkRoomResponse>>> pagination(TalkRoomSearchRequest search) {
        Pagination<WtalkRoomEntity> pagination;
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));

        if (search.getStartDate() != null && search.getEndDate() != null)
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");
        if (Objects.isNull(search.getSort()))
            search.setSorts(TalkRoomSearchRequest.Sorts.START_TIME);
        if (StringUtils.isEmpty(search.getSequence()))
            search.setSequence("desc");

        if (RoomStatus.PROCESS_OR_STOP.getCode().equals(search.getRoomStatus())) {
            pagination = currentWtalkRoomRepository.pagination(search);
        } else if (RoomStatus.DOWN.getCode().equals(search.getRoomStatus())) {
            pagination = wtalkRoomService.getRepository().pagination(search);
        } else
            throw new IllegalArgumentException(message.getText("messages.validator.invalid", "방상태 (진행.중지/내려짐)"));

        final List<WtalkRoomResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    final WtalkRoomResponse talkRoomResponse = convertDto(e, WtalkRoomResponse.class);
                    talkRoomResponse.setIdName(personListMap.getOrDefault(e.getUserid(), ""));
                    talkRoomResponse.setChannelType(TalkChannelType.of(e.getChannelType()));

                    return talkRoomResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<WtalkRoomResponse>> get(@PathVariable Integer seq, @RequestParam String roomStatus) {
        if (StringUtils.isEmpty(roomStatus))
            throw new IllegalArgumentException(message.getText("messages.validator.blank", "방상태"));

        WtalkRoomEntity roomEntity;
        if (RoomStatus.PROCESS.getCode().equals(roomStatus)
                || RoomStatus.STOP.getCode().equals(roomStatus)
                || RoomStatus.PROCESS_OR_STOP.getCode().equals(roomStatus)
                || RoomStatus.BOT_PLAY.getCode().equals(roomStatus)
                || RoomStatus.SERVICE_BY_GROUP_CONNECT.getCode().equals(roomStatus))
            roomEntity = currentWtalkRoomRepository.findOne(seq);
        else if (RoomStatus.DOWN.getCode().equals(roomStatus))
            roomEntity = wtalkRoomService.getRepository().findOneIfNullThrow(seq);
        else
            throw new IllegalArgumentException(message.getText("\"messages.validator.invalid", "방상태"));

        if (ObjectUtils.isEmpty(roomEntity) && !RoomStatus.DOWN.getCode().equals(roomStatus))
            roomEntity = wtalkRoomService.getRepository().findOneIfNullThrow(seq);

        final WtalkRoomResponse talkRoomResponse = convertDto(roomEntity, WtalkRoomResponse.class);
        if (roomEntity.getUserid() != null && !roomEntity.getUserid().equals(""))
            talkRoomResponse.setIdName(personListRepository.findOne(roomEntity.getUserid()).getIdName());
        talkRoomResponse.setChannelType(TalkChannelType.of(roomEntity.getChannelType()));

        return ResponseEntity.ok(data(talkRoomResponse));
    }

    @GetMapping("roomInfo")
    public ResponseEntity<JsonResult<WtalkRoomResponse>> get(@RequestParam String roomId) {

        WtalkRoomEntity roomEntity;
        roomEntity = currentWtalkRoomRepository.findOne(CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId));
        if(Objects.isNull(roomEntity))
            roomEntity = wtalkRoomService.getRepository().findOne(new CommonWtalkRoom(g.getUser().getCompanyId()).ROOM_ID.eq(roomId));

        final WtalkRoomResponse talkRoomResponse = convertDto(roomEntity, WtalkRoomResponse.class);
        talkRoomResponse.setIdName(personListRepository.findOne(roomEntity.getUserid()).getIdName());
        talkRoomResponse.setChannelType(TalkChannelType.of(roomEntity.getChannelType()));
        return ResponseEntity.ok(data(talkRoomResponse));
    }

    /**
     * 대화내용 보기
     */
    @GetMapping("{roomId}/message")
    public ResponseEntity<JsonResult<List<WtalkMsgResponse>>> messageHistory(@PathVariable String roomId) {
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));

        return ResponseEntity.ok(data(
                wtalkMsgService.getRepository().findAll(roomId).stream()
                        .map(msg -> {
                            final WtalkMsgResponse response = convertDto(msg, WtalkMsgResponse.class);
                            response.setIdName(personListMap.getOrDefault(msg.getUserid(), ""));

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
