package kr.co.eicn.ippbx.server.controller.api.v1.admin.sounds.sound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MohList;
import kr.co.eicn.ippbx.model.dto.eicn.MohDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MohListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryMohListResponse;
import kr.co.eicn.ippbx.model.form.MohListRequest;
import kr.co.eicn.ippbx.model.search.MohListSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.MohListRepository;
import kr.co.eicn.ippbx.server.service.MohListService;
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
@RequestMapping(value = "api/v1/admin/sounds/ring-back-tone", produces = MediaType.APPLICATION_JSON_VALUE)
public class MohListController extends ApiBaseController {

	private final MohListService service;
	private final StorageService fileSystemStorageService;
	private final MohListRepository repository;
	@Value("${file.path.moh}")
	private String savePath;

	@GetMapping("")
	public ResponseEntity<JsonResult<Pagination<MohListSummaryResponse>>> pagination(MohListSearchRequest search) {
		final Pagination<MohList> pagination = repository.pagination(search);
		final List<MohListSummaryResponse> rows = pagination.getRows().stream()
				.map((e) -> convertDto(e, MohListSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
	}

	@GetMapping(value = "{category}")
	public ResponseEntity<JsonResult<MohDetailResponse>> get(@PathVariable String category) {
		return ResponseEntity.ok()
				.body(data(convertDto(service.findOneIfNullThrow(category), MohDetailResponse.class)));
	}

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<JsonResult<String>> post(@Valid MohListRequest form, BindingResult bindingResult) throws IOException {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/sounds/ring-back-tone"))
				.body(data(service.insertOnGeneratedKeyAllPbxServersWithFileStore(form)));
	}

	@DeleteMapping(value = "{category}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable String category) {
		service.deleteWithFileStore(category);
		return ResponseEntity.ok(create());
	}

	@PutMapping("{category}/web-log")
	public ResponseEntity<JsonResult<Void>> updateWebLog(@PathVariable String category , @RequestParam String type) {
		service.updateWebLog(category, type);
		return ResponseEntity.ok(create());
	}

	@GetMapping(value = "{category}/resource")
	public ResponseEntity<Resource> resource(@PathVariable String category, @RequestParam("type") String type) {
		final MohList entity = service.findOneIfNullThrow(category);

		final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(savePath, entity.getDirectory()), entity.getFileName());

		return ResponseEntity.ok()
				.contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
				.headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
				.body(resource);
	}

	/**
	 *  대기음원 목록 조회
	 */
	@GetMapping("ring-back-tones")
	public ResponseEntity<JsonResult<List<SummaryMohListResponse>>> ringBackTone() {
		return ResponseEntity.ok(
				data(repository.findAll().stream()
						.map((e) -> convertDto(e, SummaryMohListResponse.class))
						.sorted(comparing(SummaryMohListResponse::getMohName))
						.collect(Collectors.toList())
				)
		);
	}
}
