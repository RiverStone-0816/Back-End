package kr.co.eicn.ippbx.server.controller.api.v1.admin.sounds.sound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.model.dto.eicn.SoundDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SoundListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.form.SoundListRequest;
import kr.co.eicn.ippbx.model.search.SoundListSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SoundListRepository;
import kr.co.eicn.ippbx.server.service.SoundListService;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/sounds/ars", produces = MediaType.APPLICATION_JSON_VALUE)
public class SoundListApiController extends ApiBaseController {

    private final SoundListService service;
    private final SoundListRepository repository;
    private final StorageService fileSystemStorageService;
    @Value("${file.path.ars}")
    private String savePath;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<SoundListSummaryResponse>>> list(SoundListSearchRequest search) {
        final List<SoundList> list = repository.findAll(search);
        final List<SoundListSummaryResponse> rows = list.stream()
                .map((e) -> convertDto(e, SoundListSummaryResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("search")
    public ResponseEntity<JsonResult<Pagination<SoundListSummaryResponse>>> pagination(SoundListSearchRequest search) {
        final Pagination<SoundList> pagination = service.pagination(search);
        final List<SoundListSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> convertDto(e, SoundListSummaryResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<SoundDetailResponse>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok()
                .body(data(convertDto(repository.findOneIfNullThrow(seq), SoundDetailResponse.class)));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Integer>> post(@Valid SoundListRequest form, BindingResult bindingResult) throws IOException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("/api/v1/admin/sounds/ars"))
                .body(data(service.insertOnGeneratedKeyAllPbxServersWithFileStore(form)));
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        service.deleteWithFileStore(seq);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{seq}/web-log")
    public ResponseEntity<JsonResult<Void>> updateWebLog(@PathVariable Integer seq , @RequestParam String type) {
        service.updateWebLog(seq, type);
        return ResponseEntity.ok(create());
    }

    @GetMapping(value = "{seq}/resource")
    public ResponseEntity<Resource> resource(@PathVariable Integer seq) {
        final SoundList entity = service.findOneIfNullThrow(seq);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(savePath, g.getUser().getCompanyId()), entity.getSoundFile());

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }

    @GetMapping("add-sounds-list")
    public ResponseEntity<JsonResult<List<SummarySoundListResponse>>> addSoundList() {
        return ResponseEntity.ok(data(repository.findAll()
                .stream()
                .sorted(comparing(SoundList::getSoundName))
                .map(e -> convertDto(e, SummarySoundListResponse.class))
                .collect(Collectors.toList()))
        );
    }

    @GetMapping("ars-sounds-list")
    public ResponseEntity<JsonResult<List<SummarySoundListResponse>>> arsSoundList() {
        return ResponseEntity.ok(data(repository.findAllArs()
                .stream()
                .sorted(comparing(SoundList::getSoundName))
                .map(e -> convertDto(e, SummarySoundListResponse.class))
                .collect(Collectors.toList()))
        );
    }
}
