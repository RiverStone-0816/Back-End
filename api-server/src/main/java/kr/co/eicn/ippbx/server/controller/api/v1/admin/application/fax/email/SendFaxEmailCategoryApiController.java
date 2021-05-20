package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.fax.email;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.model.form.*;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SendFaxEmailCategoryRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.*;

/**
 * 상담어플리케이션 관리 > FAX/EMAIL 관리 > 카테고리 관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/fax-email/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendFaxEmailCategoryApiController extends ApiBaseController {

    private final SendFaxEmailCategoryRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendFaxEmailCategoryResponse>>> pagination(SendCategorySearchRequest search) {
        final Pagination<SendCategory> pagination = repository.pagination(search);

        final List<SendFaxEmailCategoryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final SendFaxEmailCategoryResponse response = convertDto(e, SendFaxEmailCategoryResponse.class);
                    response.setSendMedia(Objects.equals(SendCategoryType.FAX.getCode(), e.getCategoryCode().substring(0,1)) ? SendCategoryType.FAX : SendCategoryType.EMAIL);

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{categoryCode}")
    public ResponseEntity<JsonResult<SendFaxEmailCategoryResponse>> get(@PathVariable String categoryCode) {
        final SendFaxEmailCategoryResponse response = convertDto(repository.findOneIfNullThrow(categoryCode), SendFaxEmailCategoryResponse.class);
        response.setSendMedia(Objects.equals(SendCategoryType.FAX.getCode(), response.getCategoryCode().substring(0,1)) ? SendCategoryType.FAX : SendCategoryType.EMAIL);

        return ResponseEntity.ok(data(response));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<String>> post(@Valid @RequestBody SendFaxEmailCategoryFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/application/fax-email/category")).body(create());
    }

    @PutMapping(value = "{categoryCode}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody SendCategoryUpdateRequest form, BindingResult bindingResult,
                                                @PathVariable String categoryCode) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, categoryCode);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{categoryCode}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String categoryCode) {
        repository.deleteOnIfNullThrow(categoryCode);
        return ResponseEntity.ok(create());
    }

    /**
     * 카테고리 조회
     */
    @GetMapping("category")
    public ResponseEntity<JsonResult<List<SendSmsCategorySummaryResponse>>> sendFaxEmailCategory() {
        return ResponseEntity.ok(data(repository.findAll().stream().filter(category -> !category.getCategoryType().equals(SendCategoryType.SMS.getCode()))
                .map(e -> convertDto(e, SendSmsCategorySummaryResponse.class)).collect(Collectors.toList())));
    }
}
