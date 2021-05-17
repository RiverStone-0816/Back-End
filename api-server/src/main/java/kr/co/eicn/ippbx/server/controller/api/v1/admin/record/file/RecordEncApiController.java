package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.file;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEnc;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncKey;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeyResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.RecordEncFormRequest;
import kr.co.eicn.ippbx.server.model.form.RecordEncKeyFormRequest;
import kr.co.eicn.ippbx.server.model.search.RecordEncSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.RecordEncKeyRepository;
import kr.co.eicn.ippbx.server.repository.eicn.RecordEncRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import kr.co.eicn.ippbx.server.util.spring.IsAdmin;
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

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.RecordEncKey.RECORD_ENC_KEY;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 녹취관리 > 녹취파일관리 > 녹취암호관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/file/enc", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordEncApiController extends ApiBaseController {

	private final RecordEncRepository repository;
	private final RecordEncKeyRepository recordEncKeyRepository;

	/**
	 * 녹취암호관리 목록조회
	 */
	@GetMapping("key")
	public ResponseEntity<JsonResult<Pagination<RecordEncKeySummaryResponse>>> pagination(RecordEncSearchRequest search) {
		final Pagination<RecordEncKey> pagination = recordEncKeyRepository.pagination(search);
		final List<RecordEncKeySummaryResponse> rows = pagination.getRows().stream()
				.map((e) -> convertDto(e, RecordEncKeySummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
	}

	/**
	 * 녹취파일 암호화방식 조회
	 *  -- 고객사의 암호화방식 정보 조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<RecordEnc>> get() {
		return ResponseEntity.ok(data(repository.findOne(g.getUser().getCompanyId())));
	}

	/**
	 * 암호키 조회
	 */
	@GetMapping("key/{id}")
	public ResponseEntity<JsonResult<RecordEncKeyResponse>> get(@PathVariable Integer id) {
		return ResponseEntity.ok(data(convertDto(recordEncKeyRepository.findOneIfNullThrow(id), RecordEncKeyResponse.class)));
	}

	/**
	 * 녹취파일 암호화 추가
	 */
	@IsAdmin
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody RecordEncFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/record/file/enc")).body(create());
	}

	/**
	 * 암호키 추가
	 */
	@IsAdmin
	@PostMapping("key")
	public ResponseEntity<JsonResult<Integer>> keyRegister(@Valid @RequestBody RecordEncKeyFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/record/file/enc/key")).body(data(recordEncKeyRepository.insertOnGeneratedKey(form).getValue(RECORD_ENC_KEY.ID)));
	}

	/**
	 * 암호키 수정
	 */
	@IsAdmin
	@PutMapping("key/{id}")
	public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody RecordEncKeyFormRequest form, BindingResult bindingResult, @PathVariable Integer id) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		recordEncKeyRepository.updateByKey(form, id);
		return ResponseEntity.ok(create());
	}

	/**
	 * 암호화 삭제
	 */
	@IsAdmin
	@DeleteMapping("key/{id}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer id) {
		recordEncKeyRepository.deleteByKey(id);
		return ResponseEntity.ok(create());
	}
}
