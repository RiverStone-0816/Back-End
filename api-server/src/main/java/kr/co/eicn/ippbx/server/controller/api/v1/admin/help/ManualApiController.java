package kr.co.eicn.ippbx.server.controller.api.v1.admin.help;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.BoardInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ManualFileEntity;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ManualXFile;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.model.dto.eicn.BoardSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.FileNameDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ManualDetailResponse;
import kr.co.eicn.ippbx.server.model.form.ManualFormRequest;
import kr.co.eicn.ippbx.server.model.search.BoardSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ManualFileEntityRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ManualRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ManualXFileRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.FileSystemStorageService;
import kr.co.eicn.ippbx.server.service.ManualFileUploadService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * 서비스운영관리 > 도움말 > 매뉴얼
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/help/manual", produces = MediaType.APPLICATION_JSON_VALUE)
public class ManualApiController extends ApiBaseController {

    private final ManualRepository repository;
    private final ManualFileEntityRepository fileEntityRepository;
    private final ManualXFileRepository xFileRepository;
    private final PersonListRepository personListRepository;
    private final ManualFileUploadService service;
    private final FileSystemStorageService fileSystemStorageService;

    @GetMapping
    public ResponseEntity<JsonResult<Pagination<BoardSummaryResponse>>> pagination(BoardSearchRequest search) {
        final Pagination<BoardInfo> pagination = repository.pagination(search);
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));

        final List<BoardSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final BoardSummaryResponse response = convertDto(e, BoardSummaryResponse.class);
                    if (Objects.nonNull(personListMap.get(e.getCreatorId())))
                        response.setWriter(personListMap.get(e.getCreatorId()));
                    else
                        response.setWriter(e.getCreatorId());

                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<JsonResult<ManualDetailResponse>> get(@PathVariable Long id) {
        final BoardInfo manual = repository.findOneCheckBoardType(id, false);
        final List<ManualFileEntity> fileEntityMap = fileEntityRepository.findAll();
        final List<Long> fileIds = xFileRepository.findAll().stream().filter(e -> e.getManual().equals(id)).map(ManualXFile::getFile).collect(Collectors.toList());

        final ManualDetailResponse response = convertDto(manual, ManualDetailResponse.class);
        response.setWriter(personListRepository.findOneById(manual.getCreatorId()).getIdName());

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
        return ResponseEntity.ok(data(response));
    }

    @GetMapping(value = "{id}/detail")
    public ResponseEntity<JsonResult<ManualDetailResponse>> getDetail(@PathVariable Long id) {
        final BoardInfo manual = repository.findOneCheckBoardType(id, true);
        final List<ManualFileEntity> fileEntityMap = fileEntityRepository.findAll();
        final List<Long> fileIds = xFileRepository.findAll().stream().filter(e -> e.getManual().equals(id)).map(ManualXFile::getFile).collect(Collectors.toList());

        final ManualDetailResponse response = convertDto(manual, ManualDetailResponse.class);
        response.setWriter(personListRepository.findOneById(manual.getCreatorId()).getIdName());

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
        return ResponseEntity.ok(data(response));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> post(@Valid ManualFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (form.getFiles() != null) {
            service.insertManualWithFileStore(form);
        } else {
            repository.insertOnGeneratedKey(form);
        }
        return ResponseEntity.created(URI.create("api/v1/admin/help/manual")).body(create());
    }

    @PostMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> put(@Valid ManualFormRequest form, BindingResult bindingResult,
                                                @PathVariable Long id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.updateManualWithFileStore(form, id);
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
    @GetMapping(value = "{fileId}/specific-file-resource", params = {"token"})
    public ResponseEntity<Resource> specificFileResource(@PathVariable Long fileId, HttpServletRequest request) {
        final ManualFileEntity entity = service.findOneIfNullThrow(fileId);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(getFullPath(entity.getPath())), getName(entity.getPath()));

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers -> headers.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(entity.getOriginalName()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
