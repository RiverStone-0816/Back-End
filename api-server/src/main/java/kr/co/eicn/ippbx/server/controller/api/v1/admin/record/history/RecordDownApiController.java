package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.history;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RecordDown;
import kr.co.eicn.ippbx.model.dto.eicn.RecordDownSummaryResponse;
import kr.co.eicn.ippbx.model.enums.RecordDownStatusKind;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.search.RecordDownSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebSecureHistoryRepository;
import kr.co.eicn.ippbx.server.service.RecordDownService;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.replace;

/**
 * 녹취관리 > 녹취/통화이력조회 > 일괄녹취다운관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/history/record-multi-down", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordDownApiController extends ApiBaseController {

	private final RecordDownService service;
	private final StorageService fileSystemStorageService;
	private final PersonListRepository personListRepository;
	private final WebSecureHistoryRepository webSecureHistoryRepository;
	@Value("${file.path.record-multi-down}")
	private String savePath;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<RecordDownSummaryResponse>>> list(RecordDownSearchRequest search) {
		if (Objects.isNull(search.getStartDate()))
			search.setStartDate(Date.valueOf(LocalDateTime.now().minusYears(1).toLocalDate()));
		if (Objects.isNull(search.getEndDate()))
			search.setEndDate(Date.valueOf(LocalDate.now()));
		if (search.getStartDate().after(search.getEndDate()))
			throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");

		final Map<String, String> personMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
		final List<RecordDownSummaryResponse> rows = service.list(search).stream()
				.map((e) -> {
					final RecordDownSummaryResponse entity = convertDto(e, RecordDownSummaryResponse.class);
					if (Objects.nonNull(personMap.get(e.getUserid())))
						entity.setUserName(personMap.get(e.getUserid()));
					else {
						PersonList user = personListRepository.findOneById(e.getUserid());
						entity.setUserName(user != null ? user.getIdName() : e.getUserid());
					}

					final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()), entity.getDownFolder() + ".tar.gz");
					if (Files.notExists(path)) {
						entity.setStatus(RecordDownStatusKind.IN_READY.getCode());
					} else {
						try {
							entity.setSize(Files.size(path));
						} catch (IOException ignored) {
						}
					}

					return entity;
				})
				.collect(toList());

		return ResponseEntity.ok(data(rows));
	}

	@DeleteMapping(value = "{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		service.deleteWithFileStore(seq);
		return ResponseEntity.ok(create());
	}

	@GetMapping(value = "{seq}/resource", params = {"token"})
	public ResponseEntity<Resource> resource(@PathVariable Integer seq) {
		final RecordDown entity = service.findOneIfNullThrow(seq);
		if (!RecordDownStatusKind.COMPLETE.getCode().equals(entity.getStatus()))
			throw new IllegalArgumentException("준비중입니다.");

		final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
		final Resource resource = this.fileSystemStorageService.loadAsResource(path, entity.getDownFolder() + ".tar.gz");

		webSecureHistoryRepository.insert(WebSecureActionType.RECORD_DOWN, WebSecureActionSubType.DOWN, entity.getDownFolder());

		return ResponseEntity.ok()
				.contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
				.headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
				.body(resource);
	}
}
