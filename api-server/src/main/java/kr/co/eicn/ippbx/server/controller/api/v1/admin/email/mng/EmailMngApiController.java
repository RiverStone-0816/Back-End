package kr.co.eicn.ippbx.server.controller.api.v1.admin.email.mng;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailServiceInfo;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMngDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMngSummaryResponse;
import kr.co.eicn.ippbx.model.form.EmailMngFormRequest;
import kr.co.eicn.ippbx.model.search.EmailMngSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EmailMngRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 이메일상담관리 > 이메일설정관리 > 이메일설정관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/email/mng", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailMngApiController extends ApiBaseController {
    protected final Logger logger = LoggerFactory.getLogger(EmailMngApiController.class);

    private final EmailMngRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<EmailMngSummaryResponse>>> pagination(EmailMngSearchRequest search) {
        final Pagination<EmailServiceInfo> pagination = repository.pagination(search);
        final List<EmailMngSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> convertDto(e, EmailMngSummaryResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<EmailMngDetailResponse>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok()
                .body(data(convertDto(repository.findOneIfNullThrow(seq), EmailMngDetailResponse.class)));
    }

    @PostMapping
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody EmailMngFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        form.setCompanyId(g.getUser().getCompanyId());
        repository.insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/email/mng")).body(create());
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody EmailMngFormRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        form.setCompanyId(g.getUser().getCompanyId());
        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }
}
