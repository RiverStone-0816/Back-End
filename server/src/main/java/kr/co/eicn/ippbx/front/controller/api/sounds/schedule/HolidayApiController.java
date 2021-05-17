package kr.co.eicn.ippbx.front.controller.api.sounds.schedule;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.HolidayApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.ScheduleGroupApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.form.HolyInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.ScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.server.model.search.OutScheduleSeedSearchRequest;
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
@RequestMapping(value = "api/holiday", produces = MediaType.APPLICATION_JSON_VALUE)
public class HolidayApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayApiController.class);

    @Autowired
    private HolidayApiInterface apiInterface;

    @GetMapping("")
    public List<HolyInfoResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{seq}")
    public HolyInfoResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody HolyInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody HolyInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
