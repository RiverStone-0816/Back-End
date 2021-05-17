package kr.co.eicn.ippbx.server.controller.api.v1.admin.acd.grade;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RouteApplication;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.RouteApplicationEntity;
import kr.co.eicn.ippbx.server.model.form.RAFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.RouteApplicationFormRequest;
import kr.co.eicn.ippbx.server.model.search.RouteApplicationSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.RouteApplicationRepository;
import kr.co.eicn.ippbx.server.service.RouteApplicationService;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * ACD > 고객등급 Routing > 라우팅 신청 관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/acd/grade/routeapp", produces = MediaType.APPLICATION_JSON_VALUE)
public class RouteApplicationApiController extends ApiBaseController {

    private final RouteApplicationService service;
    private final StorageService fileSystemStorageService;
    private final RouteApplicationRepository repository;
    private final PersonListRepository personListRepository;

    @GetMapping("")
    public JsonResult<Pagination<RouteApplicationEntity>> pagination(RouteApplicationSearchRequest search) {
        return data(repository.pagination(search));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<RouteApplicationEntity>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok(data(repository.findOneIfNullThrow(seq)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody RouteApplicationFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insert(form);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{seq}/accept")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody RAFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.accept(form, seq);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{seq}/reject")
    public ResponseEntity<JsonResult<Void>> put(@PathVariable Integer seq) {
        repository.reject(seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("person")
    public ResponseEntity<JsonResult<List<SearchPersonListResponse>>> person() {
        final List<SearchPersonListResponse> list = personListRepository.findAll().stream()
                .map((e) -> convertDto(e, SearchPersonListResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping(value = "{seq}/resource", params = {"token"})
    public ResponseEntity<Resource> resource(@PathVariable Integer seq) {
        final RouteApplication entity = service.findOneIfNullThrow(seq);

        final Resource resource = this.fileSystemStorageService.loadAsResource(entity.getRecordFile().substring(0, entity.getRecordFile().lastIndexOf("/")), entity.getRecordFile().split("/")[5]);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
