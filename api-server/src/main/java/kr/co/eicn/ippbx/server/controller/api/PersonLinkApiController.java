package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonLink;
import kr.co.eicn.ippbx.model.form.PersonLinkFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonLinkRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 공통 고객사 API 인터페이스
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/person-link", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonLinkApiController extends ApiBaseController {

    private final PersonLinkRepository repository;

    @GetMapping("")
    public JsonResult<List<PersonLink>> list() {
        if (!g.isLogin())
            throw new AccessDeniedException("로그인 후 사용 가능");

        return JsonResult.data(repository.findAllByPersonId(g.getUser().getId()));
    }

    @GetMapping("{seq}")
    public JsonResult<PersonLink> get(@PathVariable Integer seq) {
        return JsonResult.data(repository.findOne(seq));
    }

    @PostMapping("")
    public JsonResult<Integer> post(@Valid @RequestBody PersonLinkFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return JsonResult.data(repository.insert(form));
    }

    @PutMapping("{seq}")
    public JsonResult<Void> put(@PathVariable Integer seq, @Valid @RequestBody PersonLinkFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.update(seq, form);
        return JsonResult.create();
    }

    @DeleteMapping("{seq}")
    public JsonResult<Void> delete(@PathVariable Integer seq) {
        repository.delete(seq);
        return JsonResult.create();
    }
}
