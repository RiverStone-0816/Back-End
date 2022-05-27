package kr.co.eicn.ippbx.front.controller.api.wtalk.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.wtalk.schedule.WtalkDayScheduleApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkScheduleInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.form.DayTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.HolyTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
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
@RequestMapping(value = "api/wtalk-schedule-day", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkDayScheduleApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkDayScheduleApiController.class);

    @Autowired
    private WtalkDayScheduleApiInterface apiInterface;

    /**
     * 상담톡 일별스케쥴러 목록조회
     */
    @GetMapping("")
    public List<WtalkServiceInfoResponse> list(TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    /**
     * 상담톡 일별스케쥴러 유형보기
     */
    @GetMapping("service/type/{parent}")
    public WtalkScheduleGroupEntity getType(@PathVariable Integer parent) throws IOException, ResultFailException {
        return apiInterface.getType(parent);
    }

    /**
     * 상담톡 일별스케쥴러 유형 상세조회
     */
    @GetMapping("{seq}")
    public WtalkScheduleInfoDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
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
    public List<SummaryWtalkScheduleInfoResponse> scheduleInfos() throws IOException, ResultFailException {
        return apiInterface.scheduleInfos();
    }

    /**
     * 관련상담톡서비스 목록조회
     */
    @GetMapping("add-services")
    public List<SummaryWtalkServiceResponse> talkServices() throws IOException, ResultFailException {
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
