package kr.co.eicn.ippbx.front.controller.api.sounds.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.OutboundWeekScheduleApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.OutScheduleSeedDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.model.form.OutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.model.search.OutScheduleSeedSearchRequest;
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
@RequestMapping(value = "api/outbound-week-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class OutboundWeekScheduleApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundWeekScheduleApiController.class);

    @Autowired
    private OutboundWeekScheduleApiInterface apiInterface;

    /**
     * 스케쥴러 목록조회
     */
    @GetMapping("")
    public List<OutScheduleSeedEntity> list(OutScheduleSeedSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    /**
     * 스케쥴러 상세조회
     */
    @GetMapping("{parent}")
    public OutScheduleSeedDetailResponse get(@PathVariable Integer parent) throws IOException, ResultFailException {
        return apiInterface.get(parent);
    }

    /**
     * 스케쥴러 추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody OutScheduleSeedFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 스케쥴러 수정
     */
    @PutMapping("{parent}")
    public void update(@Valid @RequestBody OutScheduleSeedFormRequest form, BindingResult bindingResult, @PathVariable Integer parent) throws IOException, ResultFailException {
        apiInterface.update(parent, form);
    }

    /**
     * 스케쥴러 삭제
     */
    @DeleteMapping("{parent}")
    public void delete(@PathVariable Integer parent) throws IOException, ResultFailException {
        apiInterface.delete(parent);
    }

    /**
     * 내선번호 목록조회
     */
    @GetMapping("add-extensions")
    public List<SummaryPhoneInfoResponse> addExtensions() throws IOException, ResultFailException {
        return apiInterface.addExtensions();
    }

    /**
     * 음원 목록 조회
     */
    @GetMapping("add-sounds")
    public List<SummarySoundListResponse> addSounds() throws IOException, ResultFailException {
        return apiInterface.addSounds();
    }
}
