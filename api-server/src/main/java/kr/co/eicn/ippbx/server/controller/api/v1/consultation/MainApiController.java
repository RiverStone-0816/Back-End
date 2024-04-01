package kr.co.eicn.ippbx.server.controller.api.v1.consultation;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonWtalkMsg;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.WtalkRoom;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonWtalkRoom;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.records.CommonWtalkMsgRecord;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoKind;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentWtalkRoom;
//import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkRoom;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServiceInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatServiceInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.model.dto.customdb.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbMultichannelInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.WtalkRoomEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.*;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.model.search.TodoListSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.*;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.UrlUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.springframework.util.StringUtils.cleanPath;

/**
 * 상담화면_메인
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/consultation/main", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainApiController extends ApiBaseController {
    private final MaindbGroupRepository maindbGroupRepository;
    private final EicnCdrService eicnCdrService;
    private final MaindbMultichannelInfoService multichannelInfoService;
    private final ServiceRepository serviceRepository;
    private final Number070Repository number070Repository;
    private final QueueNameRepository queueNameRepository;
    private final PhoneInfoRepository phoneInfoRepository;
    private final MaindbMultichannelInfoService maindbMultichannelInfoService;
    private final TodoListRepository todoListRepository;
    private final WtalkRoomService wtalkRoomService;
    private final CurrentWtalkRoomRepository currentWtalkRoomRepository;
    private final CurrentWtalkRoomService currentWtalkRoomService;
    private final WtalkServiceInfoRepository wtalkServiceInfoRepository;
    private final WebchatServiceInfoRepository webchatServiceInfoRepository;
    private final WtalkMsgService wtalkMsgService;
    private final PersonListRepository personListRepository;
    private final CallbackRepository callbackRepository;
    private final MaindbCustomInfoService maindbCustomInfoService;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.chatbot}")
    private String savePath;

    /**
     * 전화번호 검색
     */
    @GetMapping("multichannel-info")
    public ResponseEntity<JsonResult<List<MaindbMultichannelInfoEntity>>> multichannelInfo(@RequestParam String channelType, @RequestParam String channelData) {
        Objects.requireNonNull(ChannelType.of(channelType));
        return ResponseEntity.ok(data(maindbMultichannelInfoService.getRepository().findAll()));
    }

    /**
     * 전화번호 검색
     */
    @GetMapping("custom-info")
    public ResponseEntity<JsonResult<List<CustomMultichannelInfoResponse>>> getCustomInfoList(@RequestParam(required = false) String channelData) {
        return ResponseEntity.ok(data(maindbMultichannelInfoService.getRepository().findAllCustomInfo(channelData)));
    }

    /**
     * 수신경로
     */
    @GetMapping(value = "calling-path")
    public ResponseEntity<JsonResult<List<MainReceivePathResponse>>> callingPath(@RequestParam String iniNum, @RequestParam(required = false) String secondNum) {
        final List<MainReceivePathResponse> numberList = number070Repository.findOneByNumbers(iniNum, secondNum);

        final List<MainReceivePathResponse> responseList = numberList.stream()
                .map(e -> {
                    final MainReceivePathResponse pathResponse = convertDto(e, MainReceivePathResponse.class);

                    if (NumberType.HUNT.getCode().equals(e.getType())) {
                        pathResponse.setName(queueNameRepository.getHanNameByNumber(e.getNumber()));
                    } else if (NumberType.PERSONAL.getCode().equals(e.getType())) {
                        pathResponse.setName(phoneInfoRepository.getExtensionByNumber(e.getNumber()));
                    } else if (NumberType.SERVICE.getCode().equals(e.getType())) {
                        if (Objects.nonNull(serviceRepository.getSvcNumberByNumber(e.getNumber())))
                            pathResponse.setName(serviceRepository.getSvcNumberByNumber(e.getNumber()));
                    }
                    pathResponse.setNumber(e.getNumber());
                    pathResponse.setType(e.getType());
                    pathResponse.setPathNumber(e.getNumber().equals(iniNum) ? PathNumberType.INI : PathNumberType.SECOND);

                    return pathResponse;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(data(responseList));
    }

    /**
     * 수/발신 정보
     */
    @GetMapping(value = "in-out-info")
    public ResponseEntity<JsonResult<MainInOutInfoResponse>> inOutInfoList(@RequestParam String uniqueId) {
        final MainInOutInfoResponse response = eicnCdrService.getRepository().getCallHistoryByUniqueId(uniqueId);
        if (response.getBillsec() != null) {
            response.setCurrencyStatus(response.getBillsec() > 0 ? NormalCallStatus.NORMAL.getCode() : NormalCallStatus.NON_RECEIVING.getCode());
        }

        return ResponseEntity.ok(data(response));
    }

    /**
     * 관련고객 조회
     */
    @GetMapping(value = "maindb-multichannel-info")
    public ResponseEntity<JsonResult<MainRelationCustomersResponse>> maindbMultichannelInfo(@RequestParam String uniqueId) {
        final MainRelationCustomersResponse response = new MainRelationCustomersResponse();
        final List<CommonMultiChannelInfoResponse> infoList = multichannelInfoService.getRepository().findAllByChannelData(eicnCdrService.getRepository().getCallHistoryByUniqueId(uniqueId).getCustomNumber());
        if (infoList.size() > 0) {
            for (CommonMultiChannelInfoResponse channelInfo : infoList) {
                response.setAffiliatedGroup(maindbGroupRepository.findOneByKey(channelInfo.getMaindb_group_id()));
                response.setMultiChannelInfo(channelInfo);
            }
        }

        return ResponseEntity.ok(data(response));
    }

    /**
     * 통화이력 조회
     */
    @GetMapping("record-history")
    public ResponseEntity<JsonResult<MainCurrencyHistoryResponse>> recordHistory() {
        final MainCurrencyHistoryResponse response = new MainCurrencyHistoryResponse();
        response.setInOutInfoList(eicnCdrService.getRepository().getCallHistory().stream()
                .map(e -> {
                    final MainInOutInfoResponse historyResponse = convertDto(e, MainInOutInfoResponse.class);
                    if (historyResponse.getBillsec() != null) {
                        historyResponse.setCurrencyStatus(e.getBillsec() > 0 ? NormalCallStatus.NORMAL.getCode() : NormalCallStatus.NON_RECEIVING.getCode());
                    }
                    return historyResponse;
                })
                .collect(Collectors.toList())
        );
        response.setCallCnt(eicnCdrService.getRepository().getCallHistory().size());

        return ResponseEntity.ok(data(response));
    }

    /**
     * 발신표시 조회
     */
    @GetMapping("services")
    public ResponseEntity<JsonResult<List<SummaryServiceListResponse>>> services() {
        return ResponseEntity.ok(data(serviceRepository.findAll().stream().map(e -> convertDto(e, SummaryServiceListResponse.class)).collect(Collectors.toList())));
    }

    /**
     *  전화번호 검색시
     * 	<option value='MAINDB'>고객DB</option>          /api/v1/admin/application/maindb/custominfo
     * 	<option value='RESULT'>상담이력</option>        미작업  상담어플리케이션관리 > 고객DB관리 > 상담결과이력
     * 	<option value='CALLHIS'>통화이력</option>       /api/v1/admin/record/history
     * 	if (g_service.indexOf("PRV") > -1) {
     * 	<option value='PRV'>프리뷰</option>             미작업  아웃바운드관리 > 프리뷰 > 데이터관리
     *    }
     * 	<% if (g_service.indexOf("CB") > -1) { %>
     * 	<option value='CB'>콜백</option>                /api/v1/admin/record/callback/history
     * 	<% } %>
     */

    /**
     * * 처리상태 완료 기준
     * - 상담톡 : 상담원이 상담톡 화면에서 해당 대화방을 종료한 경우
     * - 콜백 : 콜백 통화 후 상담이력 처리상태를 완료로 저장 하거나 해당 번호가 인바운드로 다시 들어온 경우 상담원이 상담 종료 후 미처리
     * 콜백 알림창에서 상담원이 완료 처리한 경우
     * - 이관 : 이관받은 상담원이 내용 확인 후 상담이력 처리상태를 완료로 저장한 경우
     * - 예약 : 상담원이 통화 완료 후 상담이력 처리상태를 완료로 저장한 경우
     * - 프리뷰 : 상담원이 통화 완료 후 상담이력 처리상태를 완료로 저장한 경우
     */
    @GetMapping("to-do")
    public JsonResult<List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList>> getTodoList(TodoListSearchRequest search) {
        return data(todoListRepository.findAll(search));
    }

    //todolist 처리결과 수정
    @PutMapping("to-do")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TodoListUpdateFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        //!!@@## 코드정리 나중에...
        TodoList list = todoListRepository.findOne(form.getSeq());
        if (list.getTodoKind().getLiteral().equals("CALLBACK")) {
            CallbackListUpdateFormRequest callbackForm = new CallbackListUpdateFormRequest();
            if (form.getTodoStatus().getLiteral().equals(TodoListTodoStatus.DONE.getLiteral())) {
                callbackForm.setStatus(CallbackStatus.COMPLETE);
            } else if (form.getTodoStatus().getLiteral().equals(TodoListTodoStatus.ING.getLiteral())) {
                callbackForm.setStatus(CallbackStatus.PROCESSING);
            } else {
                callbackForm.setStatus(CallbackStatus.NONE);
            }
            List<Integer> seqList = new ArrayList<>();
            seqList.add(Integer.parseInt(list.getDetailConnectInfo()));
            callbackForm.setServiceSequences(seqList);

            callbackRepository.update(callbackForm);
        }

        todoListRepository.updateStatusBySeq(form);


        return ResponseEntity.ok(create());
    }

    /**
     * 고객정보조회
     */
    @GetMapping("company-info/{groupId}")
    public ResponseEntity<JsonResult<CustomInfoResponse>> getCompanyInfo(@PathVariable String groupId) {
        CustomInfoResponse response = new CustomInfoResponse();
        List<CustomInfoCodeResponse> codeList = new ArrayList<>();

        if (groupId.equals("eicn")) {
            CustomInfoCodeResponse code1 = new CustomInfoCodeResponse();
            code1.setCode("etc");
            code1.setValue("곽동우");

            CustomInfoCodeResponse code2 = new CustomInfoCodeResponse();
            code2.setCode("companyName");
            code2.setValue("곽동우");
            codeList.add(code2);

            CustomInfoCodeResponse code3 = new CustomInfoCodeResponse();
            code3.setCode("siteName");
            code3.setValue("eicn");
            codeList.add(code3);

            response.setCodeList(codeList);
            response.setNumber("01037797548");
            response.setService("VHWexlSiGh4t");
        } else if (groupId.equals("primium")) {
            CustomInfoCodeResponse code1 = new CustomInfoCodeResponse();
            code1.setCode("etc");
            code1.setValue("곽동우");

            CustomInfoCodeResponse code2 = new CustomInfoCodeResponse();
            code2.setCode("companyName");
            code2.setValue("곽동우");
            codeList.add(code2);

            CustomInfoCodeResponse code3 = new CustomInfoCodeResponse();
            code3.setCode("siteName");
            code3.setValue("primium");
            codeList.add(code3);

            CustomInfoCodeResponse code4 = new CustomInfoCodeResponse();
            code4.setCode("managerName");
            code4.setValue("상담원1");
            codeList.add(code4);

            CustomInfoCodeResponse code5 = new CustomInfoCodeResponse();
            code5.setCode("address");
            code5.setValue("korea");
            codeList.add(code5);

            response.setCodeList(codeList);
            response.setNumber("07075490789");
            response.setService("VHWexlSiGh4t");
        }

        return ResponseEntity.ok(data(response));
    }

    @GetMapping("recent-consultation-reservation")
    public ResponseEntity<JsonResult<List<TodoList>>> getRecentReservations() {
        return ResponseEntity.ok(data(todoListRepository.findRecentReservations()));
    }

    @PostMapping("consultation-reservation")
    public ResponseEntity<JsonResult<Void>> reserveConsultation(@Valid @RequestBody TodoReservationFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        todoListRepository.reserve(form);

        return ResponseEntity.ok(create());
    }

    /**
     * 상담원 to-do
     */
    @GetMapping("to-do-monitor")
    public ResponseEntity<JsonResult<List<TodoDataResponse>>> todoDataList() {
        final List<TodoDataResponse> response = new ArrayList<>();
        if ("Y".equals(g.getUser().getIsTalk())) {
            final TodoDataResponse talkResponse = todoListRepository.getTodoMonitor(TodoListTodoKind.TALK);
            response.add(talkResponse);
        }
        final TodoDataResponse callbackResponse = todoListRepository.getTodoMonitor(TodoListTodoKind.CALLBACK);
        response.add(callbackResponse);
        final TodoDataResponse reservationResponse = todoListRepository.getTodoMonitor(TodoListTodoKind.RESERVE);
        response.add(reservationResponse);
        final TodoDataResponse transferResponse = todoListRepository.getTodoMonitor(TodoListTodoKind.TRANSFER);
        response.add(transferResponse);

        return ResponseEntity.ok(data(response));
    }

    /**
     * 상담원 상담톡 리스트
     */
    @GetMapping("current-wtalk-list")
    public ResponseEntity<JsonResult<List<WtalkCurrentListResponse>>> currentTalkList(TalkCurrentListSearchRequest search) {

        if (search.getAuthType() == null || search.getAuthType().equals("")) {
            search.setAuthType("USER");
        }
        if (search.getOrderBy() == null || search.getOrderBy().equals("")) {
            search.setOrderBy("room_last_time");
        }

        Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo> talkServiceInfoMap = wtalkServiceInfoRepository.findAll(WtalkServiceInfo.WTALK_SERVICE_INFO.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .stream()
                .collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo::getSenderKey, e -> e));
        webchatServiceInfoRepository.findAll(WebchatServiceInfo.WEBCHAT_SERVICE_INFO.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .forEach(e -> {
                    final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo info = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo();
                    info.setSenderKey(e.getSenderKey());
                    info.setIsChattEnable(e.getIsChattEnable());
                    info.setKakaoServiceName(e.getWebchatServiceName());
                    info.setSeq(e.getSeq());
                    info.setCompanyId(e.getCompanyId());
                    talkServiceInfoMap.put(e.getSenderKey(), info);
                });

        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<String, String> mainDb = maindbCustomInfoService.getRepository().findAllCustomNameByCustomIdMap();
        final List<WtalkRoomEntity> talkRoomList = currentWtalkRoomService.findAll(search);
        final List<WtalkCurrentListResponse> response = talkRoomList.stream()
                .map((e) -> {
                    final WtalkCurrentListResponse data = convertDto(e, WtalkCurrentListResponse.class);
                    if (isNotEmpty(e.getSenderKey())) {
                        Optional<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo> talkServiceInfo = Optional.ofNullable(talkServiceInfoMap.get(e.getSenderKey()));
                        talkServiceInfo.ifPresent(talkService -> data.setSvcName(talkService.getKakaoServiceName()));
                    }
                    if (isNotEmpty(e.getUserid()) && isNotEmpty(personListMap.get(e.getUserid()))) {
                        data.setUserName(personListMap.get(e.getUserid()));
                    } else {
                        data.setUserName("지정안됨");
                    }
                    if ("".equals(e.getMaindbCustomName())) {
                        data.setMaindbCustomName("미등록고객");
                    }
                    if (StringUtils.isEmpty(mainDb.get(e.getMaindbCustomId()))) {
                        data.setMaindbCustomName("이름없음");
                    } else {
                        if (StringUtils.isEmpty(data.getMaindbCustomName())) {
                            data.setMaindbCustomName(mainDb.get(e.getMaindbCustomId()));
                        }
                    }

                    data.setChannelType(TalkChannelType.of(e.getChannelType()));

                    return data;
                })
                .sorted(comparing(WtalkCurrentListResponse::getRoomLastTime).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(response));
    }

    /**
     * 상담원 상담톡 내용
     */
    @GetMapping("current-wtalk-msg/{roomId}")
    public ResponseEntity<JsonResult<WtalkCurrentMsgResponse>> currentTalkMsg(@PathVariable String roomId) {

        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));

        final WtalkRoomEntity wtalkRoomEntity = currentWtalkRoomRepository.findOne(CurrentWtalkRoom.CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId));

        final WtalkCurrentMsgResponse response = new WtalkCurrentMsgResponse();

        CommonWtalkMsg table = wtalkMsgService.getRepository().getTABLE();
        final List<WtalkMsgSummaryResponse> talkMsgResponseList = wtalkMsgService.getRepository().findAll(table.COMPANY_ID.eq(g.getUser().getCompanyId())
                        .and(table.ROOM_ID.eq(wtalkRoomEntity.getRoomId()).and(table.SEND_RECEIVE.notIn("SUAN","SUAY")))
                )
                .stream()
                .map((e) -> {
                    final WtalkMsgSummaryResponse data = convertDto(e, WtalkMsgSummaryResponse.class);
                    if (isNotEmpty(e.getUserid()) && (personListMap.get(e.getUserid()) != null && isNotEmpty(personListMap.get(e.getUserid())))) {
                        data.setUserName(personListMap.get(e.getUserid()));
                    } else {
                        data.setUserName("");
                    }
                    data.setChannelType(TalkChannelType.of(e.getChannelType()));

                    return data;
                })
                .limit(300)
                .sorted(comparing(WtalkMsgSummaryResponse::getInsertTime).reversed().thenComparing(WtalkMsgSummaryResponse::getSeq).reversed())
                .collect(Collectors.toList());
        if (talkMsgResponseList.size() > 0) {
            response.setLastMsgSeq(talkMsgResponseList.get(talkMsgResponseList.size() - 1).getSeq());
        } else {
            response.setLastMsgSeq(null);
        }

        response.setTalkMsgSummaryList(talkMsgResponseList);
        String customName = wtalkRoomEntity.getMaindbCustomName();
        if (customName != null && !customName.equals("")) {
            response.setCustomName(customName);
        } else if (StringUtils.isNotEmpty(wtalkRoomEntity.getMaindbCustomId()) && StringUtils.isEmpty(customName)) {
            response.setCustomName("이름없음");
        } else {
            response.setCustomName("미등록고객");
        }
        response.setRoomName(wtalkRoomEntity.getRoomName());
        response.setRoomStatus(wtalkRoomEntity.getRoomStatus());
        response.setRoomId(wtalkRoomEntity.getRoomId());
        response.setSenderKey(wtalkRoomEntity.getSenderKey());
        response.setUserKey(wtalkRoomEntity.getUserKey());
        response.setUserId(wtalkRoomEntity.getUserid());
        response.setChannelType(TalkChannelType.of(wtalkRoomEntity.getChannelType()));
        response.setIsAutoEnable(wtalkRoomEntity.getIsAutoEnable());
        response.setIsCustomUploadEnable(wtalkRoomEntity.getIsCustomUploadEnable());

        return ResponseEntity.ok(data(response));
    }

    /**
     * 상담톡 내리기
     */
    @DeleteMapping("wtalk-remove-room/{roomId}")
    public ResponseEntity<JsonResult<Void>> talkRemoveRoom(@PathVariable String roomId) {

        final WtalkRoomEntity wtalkRoomEntity = currentWtalkRoomRepository.findOne(CurrentWtalkRoom.CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId));

        wtalkRoomEntity.setRoomStatus("X");
        wtalkRoomService.getRepository().insert(wtalkRoomEntity);

        currentWtalkRoomRepository.delete(CurrentWtalkRoom.CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId));


        CommonWtalkMsg table = wtalkMsgService.getRepository().getTABLE();
        final CommonWtalkMsgRecord commonTalkMsgRecord = new CommonWtalkMsgRecord(table);

        commonTalkMsgRecord.setSendReceive("SE");
//        commonTalkMsgRecord.setCompanyId();
        commonTalkMsgRecord.setUserid(wtalkRoomEntity.getUserid());
        commonTalkMsgRecord.setUserKey(wtalkRoomEntity.getUserKey());
        commonTalkMsgRecord.setSenderKey(wtalkRoomEntity.getSenderKey());
        commonTalkMsgRecord.setMessageId("");
        commonTalkMsgRecord.setTime("");
        commonTalkMsgRecord.setType("text");
        commonTalkMsgRecord.setContent("대화방내림");
        commonTalkMsgRecord.setAttachment("");
        commonTalkMsgRecord.setExtra("");
        commonTalkMsgRecord.setRoomId(roomId);

        wtalkMsgService.getRepository().insert(commonTalkMsgRecord);

        return ResponseEntity.ok(create());
    }

    /**
     * 상담톡 자동멘트 수정
     */
    @PutMapping("wtalk-auto-enable/{roomId}")
    public ResponseEntity<JsonResult<Void>> updateTalkAutoEnable(@PathVariable String roomId, @Valid @RequestBody TalkAutoEnableFormRequest form) {
        currentWtalkRoomRepository.updateAutoEnableByRoomId(roomId, form);

        return ResponseEntity.ok(create());
    }

    @PostMapping("file")
    public ResponseEntity<JsonResult<String>> uploadFile(@Valid @RequestParam MultipartFile file) {
        final String[] enabledFileType = {".jpg", "jpeg", ".png", ".gif", ".ppt", ".pptx", ".xls", ".xlsx", ".pdf", ".hwp", ".hwpx", ".docx", ".mp3", ".mp4", ".wav", ".zip", ".txt", ".mov", ".avi", ".wmv"};

        if (!StringUtils.endsWithAny(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase(), enabledFileType)) {
            throw new IllegalArgumentException("사용할 수 없는 확장자 입니다.");
        }

        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        final String saveFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()).concat("_") + UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename()))).replaceAll("[ ()]", "");
        fileSystemStorageService.store(newPath, saveFileName, file);

        return ResponseEntity.ok(data(saveFileName));
    }
}
