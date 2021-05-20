package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.fax.email;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFile;
import kr.co.eicn.ippbx.model.dto.eicn.SendFileResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.model.form.SendFileFormRequest;
import kr.co.eicn.ippbx.model.form.SendFileUpdateRequest;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SendFaxEmailCategoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SendFileRepository;
import kr.co.eicn.ippbx.server.service.FileSystemStorageService;
import kr.co.eicn.ippbx.server.service.SendFileService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > FAX/EMAIL 관리 > 발송물 관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/fax-email/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendFileApiController extends ApiBaseController {

    private final SendFileRepository repository;
    private final SendFaxEmailCategoryRepository categoryRepository;
    private final SendFileService service;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.send}")
    private String savePath;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendFileResponse>>> pagination(SendCategorySearchRequest search) {
        final Pagination<SendFile> pagination = repository.pagination(search);
        final Map<String, String> categoryMap = categoryRepository.findAll().stream().collect(Collectors.toMap(SendCategory::getCategoryCode, SendCategory::getCategoryName));

        final List<SendFileResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final SendFileResponse response = convertDto(e, SendFileResponse.class);
                    if(Objects.nonNull(categoryMap.get(e.getCategoryCode())))
                        response.setCategoryName(categoryMap.get(e.getCategoryCode()));
                    response.setSendMedia(Objects.equals(SendCategoryType.FAX.getCode(), e.getCategoryCode().substring(0,1)) ? SendCategoryType.FAX : SendCategoryType.EMAIL);

                    return response;
                })
                .sorted(comparing(SendFileResponse::getId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<JsonResult<SendFileResponse>> get(@PathVariable Long id) {
        final SendFile sendFile = repository.findOneIfNullThrow(id);
        final Map<String, String> categoryMap = categoryRepository.findAll().stream().collect(Collectors.toMap(SendCategory::getCategoryCode, SendCategory::getCategoryName));

        final SendFileResponse response = convertDto(sendFile, SendFileResponse.class);
        if(Objects.nonNull(categoryMap.get(sendFile.getCategoryCode())))
            response.setCategoryName(categoryMap.get(sendFile.getCategoryCode()));

        response.setSendMedia(Objects.equals(SendCategoryType.FAX.getCode(), response.getCategoryCode().substring(0,1)) ? SendCategoryType.FAX : SendCategoryType.EMAIL);
        response.setSendName(sendFile.getName());
        response.setFilePath(sendFile.getPath());

        return ResponseEntity.ok(data(response));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Long>> post(@Valid SendFileFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("api/v1/admin/application/fax-email/file"))
                .body(data(service.insertOnGeneratedKeyWithFileStore(form)));
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> put(@Valid SendFileUpdateRequest form, BindingResult bindingResult,
                                                @PathVariable Long id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.updateWithFileStore(form, id);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Long id) {
        service.deleteWithFileStore(id);
        return ResponseEntity.ok(create());
    }

    @GetMapping("category")
    public ResponseEntity<JsonResult<List<SendSmsCategorySummaryResponse>>> sendFaxEmailCategory() {
        return ResponseEntity.ok(data(categoryRepository.findAll().stream().filter(category -> !category.getCategoryType().equals(SendCategoryType.SMS.getCode()))
                .map(e -> convertDto(e, SendSmsCategorySummaryResponse.class)).collect(Collectors.toList())));
    }

    /**
     *   파일 업로드
     */
    @GetMapping(value = "{id}/resource", params = {"token"})
    public ResponseEntity<Resource> resource(@PathVariable Long id){
        final SendFile entity = service.findOneIfNullThrow(id);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(savePath, g.getUser().getCompanyId()), entity.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
