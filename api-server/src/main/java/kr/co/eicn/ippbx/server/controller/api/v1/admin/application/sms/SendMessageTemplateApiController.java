package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.sms;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.model.form.*;
import kr.co.eicn.ippbx.model.search.SendMessageTemplateSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > SMS 관리 > 상용문구 관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/sms/message-template", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendMessageTemplateApiController extends ApiBaseController {

    private final SendMessageTemplateRepository repository;
    private final SendSmsCategoryRepository sendSmsCategoryRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendMessageTemplateResponse>>> pagination(SendMessageTemplateSearchRequest search) {
        final Pagination<SendMessageTemplate> pagination = repository.pagination(search);
        final Map<String, String> categoryMap = sendSmsCategoryRepository.findAll().stream().collect(Collectors.toMap(SendCategory::getCategoryCode, SendCategory::getCategoryName));

        final List<SendMessageTemplateResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final SendMessageTemplateResponse response = convertDto(e, SendMessageTemplateResponse.class);
                    if(Objects.nonNull(categoryMap.get(e.getCategoryCode())))
                        response.setCategoryName(categoryMap.get(e.getCategoryCode()));

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<JsonResult<SendMessageTemplateResponse>> get(@PathVariable Integer id) {
        final SendMessageTemplateResponse response = convertDto(repository.findOneIfNullThrow(Long.valueOf(id)), SendMessageTemplateResponse.class);
        final Map<String, String> categoryMap = sendSmsCategoryRepository.findAll().stream().collect(Collectors.toMap(SendCategory::getCategoryCode, SendCategory::getCategoryName));

        if(Objects.nonNull(categoryMap.get(response.getCategoryCode())))
            response.setCategoryName(categoryMap.get(response.getCategoryCode()));

        return ResponseEntity.ok(data(response));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Long>> post(@Valid @RequestBody SendMessageTemplateFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/application/sms/message-template")).body(create());
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody SendMessageTemplateUpdateRequest form, BindingResult bindingResult,
                                                @PathVariable Integer id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        sendSmsCategoryRepository.findOneIfNullThrow(form.getCategoryCode());
        repository.updateByKey(form, Long.valueOf(id));
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Long id) {
        repository.deleteOnIfNullThrow(id);
        return ResponseEntity.ok(create());
    }

    /**
     *   카테고리 조회
     */
    @GetMapping("category")
    public ResponseEntity<JsonResult<List<SendSmsCategorySummaryResponse>>> sendCategory() {
        return ResponseEntity.ok(data(sendSmsCategoryRepository.findAll().stream().filter(category -> category.getCategoryType().equals(SendCategoryType.SMS.getCode()))
                .map(e -> convertDto(e, SendSmsCategorySummaryResponse.class)).collect(Collectors.toList())));
    }
}
