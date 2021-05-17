package kr.co.eicn.ippbx.front.controller.api.sounds.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.InboundDayScheduleApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.Number070ScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.Number070ScheduleInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryScheduleGroupResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.server.model.form.DayScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.DayScheduleInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.HolyScheduleInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.ScheduleInfoSearchRequest;
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
@RequestMapping(value = "api/inbound-day-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class InboundDayScheduleApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(InboundDayScheduleApiController.class);

    @Autowired
    private InboundDayScheduleApiInterface apiInterface;

    /**
     * 일별스케쥴러 목록조회
     */
    @GetMapping("")
    public List<Number070ScheduleInfoResponse> list(ScheduleInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    /**
     * 일별스케쥴러 유형보기
     */
    @GetMapping("service/type/{parent}")
    public ScheduleGroupEntity getType(@PathVariable Integer parent) throws IOException, ResultFailException {
        return apiInterface.getType(parent);
    }

    /**
     * 일별스케쥴러 일정 상세조회
     */
    @GetMapping("{seq}")
    public Number070ScheduleInfoDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 일별스케쥴러 추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody DayScheduleInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 일별스케쥴러 삭제
     */
    @DeleteMapping("{number}")
    public void delete(@PathVariable String number) throws IOException, ResultFailException {
        apiInterface.delete(number);
    }

    @PostMapping("holy")
    public void holyPost(@Valid @RequestBody HolyScheduleInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.holyPost(form);
    }

    /**
     * 스케쥴일정 수정
     */
    @DeleteMapping("service/type/{seq}")
    public void deleteType(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.deleteType(seq);
    }

    /**
     * 스케쥴일정 수정
     */
    @PutMapping("service/type/{seq}")
    public void updateType(@Valid @RequestBody DayScheduleInfoUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.updateType(seq, form);
    }

    /**
     * 검색
     * - 스케쥴러에 설정된 번호만 검색
     */
    @GetMapping("/search-number-list")
    public List<SummaryNumber070Response> searchNumbers() throws IOException, ResultFailException {
        return apiInterface.searchNumbers();
    }

    /**
     * 일별스케쥴러 스케쥴유형 목록조회
     */
    @GetMapping("/schedule-group")
    public List<SummaryScheduleGroupResponse> scheduleGroups() throws IOException, ResultFailException {
        return apiInterface.scheduleGroups();
    }

    /**
     * 추가 가능한 번호 목록 조회
     */
    @GetMapping("add-number-list")
    public List<SummaryNumber070Response> addNumber070List() throws IOException, ResultFailException {
        return apiInterface.addNumber070List();
    }
}
