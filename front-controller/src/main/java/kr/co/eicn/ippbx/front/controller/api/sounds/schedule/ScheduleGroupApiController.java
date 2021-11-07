package kr.co.eicn.ippbx.front.controller.api.sounds.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.ScheduleGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListTimeUpdateFormRequest;
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
@RequestMapping(value = "api/schedule-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleGroupApiController.class);

    @Autowired
    private ScheduleGroupApiInterface apiInterface;

    @GetMapping("")
    public List<ScheduleGroupSummaryResponse> list(OutScheduleSeedSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list();
    }

    /**
     * 스케쥴유형 추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody ScheduleGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 스케쥴유형 삭제
     */
    @DeleteMapping("{parent}")
    public void delete(@PathVariable Integer parent) throws IOException, ResultFailException {
        apiInterface.delete(parent);
    }

    /**
     * 스케쥴유형 항목 복사
     */
    @PostMapping("{parent}/{targetParent}/copy")
    public void itemCopy(@PathVariable Integer parent, @PathVariable Integer targetParent) throws IOException, ResultFailException {
        apiInterface.itemCopy(parent, targetParent);
    }

    @GetMapping("item/{child}")
    public ScheduleGroupListDetailResponse getItem(@PathVariable Integer child) throws IOException, ResultFailException {
        return apiInterface.getItem(child);
    }

    /**
     * 스케쥴유형 항목추가
     */
    @PostMapping("item")
    public void registerItem(@Valid @RequestBody ScheduleGroupListFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.registerItem(form);
    }

    /**
     * 스케쥴유형 항목수정
     */
    @PutMapping("item/{child}")
    public void updateItem(@Valid @RequestBody ScheduleGroupListFormRequest form, BindingResult bindingResult, @PathVariable Integer child) throws IOException, ResultFailException {
        apiInterface.updateItem(child, form);
    }

    /**
     * 스케쥴유형 시간 수정
     * */
    @PutMapping("item/time/{child}")
    public void updateTime(@Valid @RequestBody ScheduleGroupListTimeUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer child) throws IOException,  ResultFailException {
        apiInterface.updateTime(child, form);
    }

    /**
     * 스케쥴유형 항목삭제
     */
    @DeleteMapping("item/{child}")
    public void deleteItem(@PathVariable Integer child) throws IOException, ResultFailException {
        apiInterface.deleteItem(child);
    }

    @GetMapping("add-sounds-list")
    public List<SummarySoundListResponse> addSoundList() throws IOException, ResultFailException {
        return apiInterface.addSoundList();
    }

    @GetMapping("add-number-list")
    public List<SummaryNumber070Response> addNumber070List() throws IOException, ResultFailException {
        return apiInterface.addNumber070List();
    }

    @GetMapping("add-ivr-list")
    public List<SummaryIvrTreeResponse> addIvrTreeList() throws IOException, ResultFailException {
        return apiInterface.addIvrTreeList();
    }

    @GetMapping("add-context-list")
    public List<SummaryContextInfoResponse> addContextList() throws IOException, ResultFailException {
        return apiInterface.addContextList();
    }
}
