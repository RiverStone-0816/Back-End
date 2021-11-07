package kr.co.eicn.ippbx.front.controller.api.talk.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.schedule.TalkDayScheduleApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkScheduleInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.form.DayTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.HolyTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/talk-schedule-day", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkDayScheduleApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkDayScheduleApiController.class);

    @Autowired
    private TalkDayScheduleApiInterface apiInterface;

    /**
     * 상담톡 일별스케쥴러 목록조회
     */
    @GetMapping("")
    public List<TalkServiceInfoResponse> list(TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    /**
     * 상담톡 일별스케쥴러 유형보기
     */
    @GetMapping("service/type/{parent}")
    public TalkScheduleGroupEntity getType(@PathVariable Integer parent) throws IOException, ResultFailException {
        return apiInterface.getType(parent);
    }

    /**
     * 상담톡 일별스케쥴러 유형 상세조회
     */
    @GetMapping("{seq}")
    public TalkScheduleInfoDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 상담톡 일별스케쥴러 추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody DayTalkScheduleInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 상담톡 일별스케쥴러 삭제
     */
    @DeleteMapping("{senderKey}")
    public void delete(@PathVariable String senderKey) throws IOException, ResultFailException {
        apiInterface.delete(senderKey);
    }

    /**
     * 상담톡 일별스케쥴러 스케쥴유형 수정
     */
    @PutMapping("service/type/{seq}")
    public void updateType(@Valid @RequestBody TalkScheduleInfoFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.updateType(seq, form);
    }

    /**
     * 상담톡 일별스케쥴러 유형삭제
     */
    @DeleteMapping("service/type/{seq}")
    public void deleteType(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.deleteType(seq);
    }

    /**
     * 상담톡 일별스케쥴러 스케쥴유형 목록조회
     */
    @GetMapping("/schedule-info")
    public List<SummaryTalkScheduleInfoResponse> scheduleInfos() throws IOException, ResultFailException {
        return apiInterface.scheduleInfos();
    }

    /**
     * 관련상담톡서비스 목록조회
     */
    @GetMapping("add-services")
    public List<SummaryTalkServiceResponse> talkServices() throws IOException, ResultFailException {
        return apiInterface.talkServices();
    }

    /**
     * 공휴일 일괄등록
     */
    @PostMapping("holy")
    public void holyPost(@Valid @RequestBody HolyTalkScheduleInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.holyPost(form);
    }
}
