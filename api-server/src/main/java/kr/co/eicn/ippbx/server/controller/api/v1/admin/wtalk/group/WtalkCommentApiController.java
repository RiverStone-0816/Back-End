package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.group;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMentDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMentSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkMentRepository;
import kr.co.eicn.ippbx.util.JsonResult;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMent.WTALK_MENT;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡그룹/자동멘트관리 > 상담톡자동멘트관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/group/auto-comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkCommentApiController extends ApiBaseController {

	private final WtalkMentRepository repository;

	/**
	 * 상담톡자동멘트 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<WtalkMentSummaryResponse>>> list() {
		final List<WtalkMentSummaryResponse> list = repository.findAll().stream()
				.map((e) -> convertDto(e, WtalkMentSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(list));
	}

	/**
	 * 상담톡자동멘트 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<WtalkMentDetailResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(convertDto(repository.findOneIfNullThrow(seq), WtalkMentDetailResponse.class)));
	}

	/**
	 * 상담톡자동멘트 추가하기
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody TalkMentFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/wtalk/group/auto-comment"))
				.body(data(repository.insertOnGeneratedKey(form).getValue(WTALK_MENT.SEQ)));
	}

	/**
	 * 상담톡자동멘트 수정하기
	 */
	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TalkMentFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKey(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡자동멘트 삭제하기
	 */
	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		repository.deleteOnIfNullThrow(seq);
		return ResponseEntity.ok(create());
	}
}