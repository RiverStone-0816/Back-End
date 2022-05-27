package kr.co.eicn.ippbx.front.controller.api.wtalk.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.wtalk.schedule.WtalkScheduleGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleGroupListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupListFormRequest;
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
@RequestMapping(value = "api/wtalk-schedule-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkScheduleGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkScheduleGroupApiController.class);

    @Autowired
    private WtalkScheduleGroupApiInterface apiInterface;

    @GetMapping("")
    public List<WtalkScheduleGroupSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @PostMapping("")
    public void post(@Valid @RequestBody TalkScheduleGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @DeleteMapping("{parent}")
    public void delete(@PathVariable Integer parent) throws IOException, ResultFailException {
        apiInterface.delete(parent);
    }

    @GetMapping("item/{child}")
    public WtalkScheduleGroupListDetailResponse itemGet(@PathVariable Integer child) throws IOException, ResultFailException {
        return apiInterface.getItem(child);
    }

    /**
     * 스케쥴유형 항목추가
     */
    @PostMapping("item")
    public void itemRegister(@Valid @RequestBody TalkScheduleGroupListFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.registerItem(form);
    }

    /**
     * 스케쥴유형 항목수정
     */
    @PutMapping("item/{child}")
    public void itemUpdate(@Valid @RequestBody TalkScheduleGroupListFormRequest form, BindingResult bindingResult, @PathVariable Integer child) throws IOException, ResultFailException {
        apiInterface.updateItem(child, form);
    }

    /**
     * 스케쥴유형 항목삭제
     */
    @DeleteMapping("item/{child}")
    public void itemDelete(@PathVariable Integer child) throws IOException, ResultFailException {
        apiInterface.deleteItem(child);
    }
}
