package kr.co.eicn.ippbx.server.controller.api.v1.admin.monitor.screen;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.server.model.form.ScreenConfigFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ScreenConfigRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 모니터링 > 전광판 > 통화전광판
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/monitor/screen", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScreenConfigApiController extends ApiBaseController {

    private final ScreenConfigRepository repository;

    @GetMapping("")
    public JsonResult<List<ScreenConfigEntity>> list() {
        return JsonResult.data(repository.findAll());
    }

    @GetMapping(value = "{seq}")
    public JsonResult<ScreenConfigEntity> get(@PathVariable Integer seq) {
        return JsonResult.data(repository.findOneIfNullThrow(seq));
    }

    @PostMapping("")
    public JsonResult<Integer> post(@Valid @RequestBody ScreenConfigFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return JsonResult.data(repository.insertOnGeneratedKey(form));
    }

    @PutMapping(value = "{seq}")
    public JsonResult<Void> put(@Valid @RequestBody ScreenConfigFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.update(seq, form);
        return JsonResult.create();
    }

    @DeleteMapping(value = "{seq}")
    public JsonResult<Void> delete(@PathVariable Integer seq) {
        repository.delete(seq);
        return JsonResult.create();
    }

}
