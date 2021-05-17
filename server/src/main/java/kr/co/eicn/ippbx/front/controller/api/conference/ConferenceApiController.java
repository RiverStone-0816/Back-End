package kr.co.eicn.ippbx.front.controller.api.conference;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.model.form.ConfInfoCopyForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.conference.ConferenceApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.form.*;
import kr.co.eicn.ippbx.server.model.search.ConfInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/conference", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConferenceApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceApiController.class);

    @Autowired
    private ConferenceApiInterface apiInterface;

    @PostMapping("copy")
    public Integer copy(@Valid @RequestBody ConfInfoCopyForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        final ConfInfoFormRequest request = new ConfInfoFormRequest();

        final ConfInfoDetailResponse entity = apiInterface.get(form.getTargetSeq());
        request.setConfName(entity.getConfName());
        request.setReserveFromTime(form.getReserveFromTime());
        request.setReserveToTime(form.getReserveToTime());
        request.setConfSound(entity.getConfSound());
        request.setIsRecord(entity.getIsRecord());
        request.setConfCid(entity.getConfCid());
        request.setIsMachineDetect(entity.getIsMachineDetect());
        request.setConfPasswd(entity.getConfPasswd());
        request.setConfPeerMembers(entity.getInMemberList().stream().map(SummaryPersonResponse::getPeer).collect(Collectors.toSet()));
        request.setConfOutMembers(entity.getOutMemberList().stream().map(e -> {
            final ConfMemberOutPersonFormRequest person = new ConfMemberOutPersonFormRequest();
            person.setMemberName(e.getMemberName());
            person.setMemberNumber(e.getMemberNumber());
            return person;
        }).collect(Collectors.toSet()));
        request.setRoomNumber(form.getRoomNumber());
        request.setReserveDate(form.getReserveDate());

        return apiInterface.post(request);
    }

    /**
     * 회의실 목록조회
     */
    @GetMapping("confroom-list")
    public List<ConfRoomSummaryResponse> getConfRoomList(/*ConfRoomSearchRequest search*/) throws IOException, ResultFailException {
        return apiInterface.getConfRoomList();
    }

    /**
     * 회의실예약 목록조회
     */
    @GetMapping("")
    public List<ConfInfoSummaryResponse> list(ConfInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    /**
     * 추가 가능한 내부참여자 목록조회
     */
    @GetMapping("add_on_conf_persons")
    public List<SummaryPersonResponse> addOnConfPersons(@RequestParam(required = false) Integer seq) throws IOException, ResultFailException {
        return apiInterface.addOnConfPersons(seq);
    }

    /**
     * 회의참석시음원 목록 조회
     */
    @GetMapping("add-sounds-list")
    public List<SummarySoundListResponse> addSoundList() throws IOException, ResultFailException {
        return apiInterface.addSoundList();
    }

    /**
     * 회의예약 상세조회
     */
    @GetMapping("{seq}")
    public ConfInfoDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 회의실 예약 추가
     */
    @PostMapping("")
    public Integer post(@Valid @RequestBody ConfInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    /**
     * 회의실 예약 수정
     */
    @PutMapping(value = "{seq}")
    public void put(@Valid @RequestBody ConfInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    /**
     * 회의실 예약 부분 수정
     */
    @PutMapping("update/{seq}")
    public void update(@Valid @RequestBody ConfInfoUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.update(seq, form);
    }

    /**
     * 회의실 예약 삭제
     */
    @DeleteMapping(value = "{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     *   회의실 시간 중복 체크
     */
    /**
     * 아이디 중복체크
     */
    @PostMapping("duplicate")
    public void duplicate(@Valid @RequestBody ConfInfoDuplicateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.duplicate(form);
    }

    /**
     * 회의록 보기
     */
    @GetMapping("minutes/{confInfoId}")
    public ConfInfoMinutesResponse minutes(@PathVariable int confInfoId) throws IOException, ResultFailException {
        return apiInterface.minutes(confInfoId);
    }

    /**
     * 회의록 저장
     */
    @PostMapping("minutes-save")
    public void minutesSave(@Valid @RequestBody ConfInfoMinutesSaveFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.minutesSave(form);
    }
}
