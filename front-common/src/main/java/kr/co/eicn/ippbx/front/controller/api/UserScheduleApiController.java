package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.UserScheduleApiInterface;
import kr.co.eicn.ippbx.model.entity.eicn.UserScheduleEntity;
import kr.co.eicn.ippbx.model.form.UserScheduleFormRequest;
import kr.co.eicn.ippbx.model.search.UserScheduleSearchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/user-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserScheduleApiController extends BaseController {
    private final UserScheduleApiInterface apiInterface;

    @GetMapping("search")
    public List<UserScheduleEntity> search(UserScheduleSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.search(search);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody UserScheduleFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@PathVariable Integer seq, @Valid @RequestBody UserScheduleFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
