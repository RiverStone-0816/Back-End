package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.sms;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategoryDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.model.form.SendSmsCategoryFormRequest;
import kr.co.eicn.ippbx.model.form.SendCategoryUpdateRequest;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SendSmsCategoryRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > SMS 관리 > 카테고리 관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/sms/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendSmsCategoryApiController extends ApiBaseController {

    private final SendSmsCategoryRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendSmsCategoryDetailResponse>>> pagination(SendCategorySearchRequest search) {
        final Pagination<SendCategory> pagination = repository.pagination(search);

        final List<SendSmsCategoryDetailResponse> rows = pagination.getRows().stream()
                .map((e) -> convertDto(e, SendSmsCategoryDetailResponse.class))
                .sorted(comparing(SendSmsCategoryDetailResponse::getCategoryCode))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{categoryCode}")
    public ResponseEntity<JsonResult<SendSmsCategoryDetailResponse>> get(@PathVariable String categoryCode) {
        return ResponseEntity.ok().body(data(convertDto(repository.findOneIfNullThrow(categoryCode), SendSmsCategoryDetailResponse.class)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<String>> post(@Valid @RequestBody SendSmsCategoryFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/application/sms/category")).body(create());
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
     *   카테고리 조회
     */
    @GetMapping("list")
    public ResponseEntity<JsonResult<List<SendSmsCategorySummaryResponse>>> sendCategory() {
        return ResponseEntity.ok(data(repository.findAll().stream().filter(category -> category.getCategoryType().equals(SendCategoryType.SMS.getCode()))
                .map(e -> convertDto(e, SendSmsCategorySummaryResponse.class)).collect(Collectors.toList())));
    }
}
