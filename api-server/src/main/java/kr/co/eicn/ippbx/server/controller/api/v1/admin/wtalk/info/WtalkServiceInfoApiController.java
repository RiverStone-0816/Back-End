package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.info;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkServiceInfoRepository;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServiceInfo.WTALK_SERVICE_INFO;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡정보관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/info/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkServiceInfoApiController extends ApiBaseController {

	private final WtalkServiceInfoRepository wtalkServiceInfoRepository;

	/**
	 * 상담톡 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<WtalkServiceSummaryResponse>>> list() {
		final List<WtalkServiceSummaryResponse> list = wtalkServiceInfoRepository.findAll().stream()
				.map((e) -> convertDto(e, WtalkServiceSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(list));
	}

	/**
	 * 상담톡 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<WtalkServiceDetailResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(convertDto(wtalkServiceInfoRepository.findOneIfNullThrow(seq), WtalkServiceDetailResponse.class)));
	}

	/**
	 * 상담톡 추가하기
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody TalkServiceInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/wtalk/info/service"))
				.body(data(wtalkServiceInfoRepository.insertOnGeneratedKey(form).getValue(WTALK_SERVICE_INFO.SEQ)));
	}

	/**
	 * 상담톡 수정하기
	 */
	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TalkServiceInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		wtalkServiceInfoRepository.updateByKey(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡 삭제하기
	 */
	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		wtalkServiceInfoRepository.deleteOnIfNullThrow(seq);
		return ResponseEntity.ok(create());
	}
}
