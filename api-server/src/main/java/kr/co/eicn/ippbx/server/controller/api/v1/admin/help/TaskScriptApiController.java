package kr.co.eicn.ippbx.server.controller.api.v1.admin.help;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.TaskScriptCategoryFormRequest;
import kr.co.eicn.ippbx.model.form.TaskScriptFormRequest;
import kr.co.eicn.ippbx.model.search.TaskScriptSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.FileSystemStorageService;
import kr.co.eicn.ippbx.server.service.TaskFileUploadService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * 서비스운영관리 > 도움말 > 지식관리
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/help/script", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskScriptApiController extends ApiBaseController {

    private final TaskScriptRepository repository;
    private final TaskScriptCategoryRepository categoryRepository;
    private final TaskScriptXFileRepository xFileRepository;
    private final TaskFileEntityRepository fileEntityRepository;
    private final TaskFileUploadService service;
    private final FileSystemStorageService fileSystemStorageService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<TaskScriptSummaryResponse>>> pagination(TaskScriptSearchRequest search) {
        final Pagination<TaskScript> pagination = repository.pagination(search);
        final List<TaskScriptSummaryResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    final TaskScriptSummaryResponse response = convertDto(e, TaskScriptSummaryResponse.class);
                    response.setCategoryId(e.getCategory());

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<JsonResult<TaskScriptDetailResponse>> get(@PathVariable Long id) {
        final TaskScript taskScript = repository.findOneIfNullThrow(id);
        final List<FileEntity> fileEntityMap = fileEntityRepository.findAll();

        final List<Long> fileIds = xFileRepository.findAll().stream()
                .filter(e -> e.getTaskScript().equals(id))
                .map(TaskScriptXFile::getFile)
                .collect(Collectors.toList());

        final TaskScriptDetailResponse response = convertDto(taskScript, TaskScriptDetailResponse.class);

        if (Objects.nonNull(fileEntityMap)) {
            final List<FileNameDetailResponse> fileList = new ArrayList<>();

            for (Long fileId : fileIds) {
                fileList.add(
                        fileEntityMap.stream()
                                .filter(e -> e.getId().equals(fileId))
                                .map(e -> convertDto(e, FileNameDetailResponse.class))
                                .findFirst().orElse(null)
                );
            }
            response.setFileInfo(fileList);
        }
        response.setCategoryId(taskScript.getCategory());
        return ResponseEntity.ok(data(response));
    }

    @GetMapping("category-list")
    public ResponseEntity<JsonResult<List<TaskScriptCategoryResponse>>> taskScriptCategoryList() {
        return ResponseEntity.ok(data(categoryRepository.findAll().stream().map(e -> convertDto(e, TaskScriptCategoryResponse.class)).collect(Collectors.toList())));
    }

    /**
     * 카테고리 분류
     */
    @PostMapping(value = "post-script-category")
    public ResponseEntity<JsonResult<Void>> insertTaskScriptCategory(@Valid @RequestBody TaskScriptCategoryFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        categoryRepository.insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/help/script/post-script-category")).body(create());
    }

    @PutMapping(value = "put-script-category/{id}")
    public ResponseEntity<JsonResult<Void>> updateTaskScriptCategory(@Valid @RequestBody TaskScriptCategoryFormRequest form, BindingResult bindingResult,
                                                                     @PathVariable Long id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        categoryRepository.updateByKey(form, id);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "delete-script-category/{id}")
    public ResponseEntity<JsonResult<Void>> deleteTaskScriptCategory(@PathVariable Long id) {
        categoryRepository.deleteOnIfNullThrow(id);
        return ResponseEntity.ok(create());
    }

    /**
     * 스크립트
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Long>> post(@Valid TaskScriptFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (form.getFiles() != null)
            service.insertFileEntityWithFileStore(form);
        else
            repository.insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/help/script")).body(create());
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> put(@Valid TaskScriptFormRequest form, BindingResult bindingResult,
                                                             @PathVariable Long id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.updateNoticeWithFileStore(form, id);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Long id) {
        service.deleteFileEntityWithFileStore(id);
        return ResponseEntity.ok(create());
    }

    /**
     * 특정 파일 삭제
     */
    @DeleteMapping(value = "delete-specific-file/{id}")
    public ResponseEntity<JsonResult<Void>> deleteSpecificFile(@PathVariable Long id) {
        service.deleteSpecificFile(id);
        return ResponseEntity.ok(create());
    }

    /**
     * 특정 파일 다운로드
     */
    @GetMapping(value = "{fileId}/specific-file-resource")
    public ResponseEntity<Resource> specificFileResource(@PathVariable Long fileId, HttpServletRequest request) {
        final FileEntity entity = service.findOneIfNullThrow(fileId);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(getFullPath(entity.getPath())), getName(entity.getPath()));

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers -> headers.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(entity.getOriginalName()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
