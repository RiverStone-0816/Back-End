package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.info;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.TalkServiceInfoRepository;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkServiceInfo.TALK_SERVICE_INFO;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡정보관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/info/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkServiceInfoApiController extends ApiBaseController {

	private final TalkServiceInfoRepository talkServiceInfoRepository;

	/**
	 * 상담톡 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<TalkServiceSummaryResponse>>> list() {
		final List<TalkServiceSummaryResponse> list = talkServiceInfoRepository.findAll().stream()
				.map((e) -> convertDto(e, TalkServiceSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(list));
	}

	/**
	 * 상담톡 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<TalkServiceDetailResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(convertDto(talkServiceInfoRepository.findOneIfNullThrow(seq), TalkServiceDetailResponse.class)));
	}

	/**
	 * 상담톡 추가하기
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody TalkServiceInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/talk/info/service"))
				.body(data(talkServiceInfoRepository.insertOnGeneratedKey(form).getValue(TALK_SERVICE_INFO.SEQ)));
	}

	/**
	 * 상담톡 수정하기
	 */
	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TalkServiceInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		talkServiceInfoRepository.updateByKey(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡 삭제하기
	 */
	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		talkServiceInfoRepository.deleteOnIfNullThrow(seq);
		return ResponseEntity.ok(create());
	}
}
