package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.entity.eicn.UserScheduleEntity;
import kr.co.eicn.ippbx.model.form.UserScheduleFormRequest;
import kr.co.eicn.ippbx.model.search.UserScheduleSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.UserScheduleRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/user-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserScheduleApiController {

    private final UserScheduleRepository userScheduleRepository;

    @GetMapping("")
    public JsonResult<List<UserScheduleEntity>> search(UserScheduleSearchRequest search) {
        return data(userScheduleRepository.search(search));
    }

    @GetMapping("{seq}")
    public JsonResult<UserScheduleEntity> get(@PathVariable Integer seq) {
        return data(userScheduleRepository.findOne(seq));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody UserScheduleFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        userScheduleRepository.insert(form);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@PathVariable Integer seq, @Valid @RequestBody UserScheduleFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        userScheduleRepository.update(seq, form);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        userScheduleRepository.deleteSchedule(seq);
        return ResponseEntity.ok(create());
    }
}
