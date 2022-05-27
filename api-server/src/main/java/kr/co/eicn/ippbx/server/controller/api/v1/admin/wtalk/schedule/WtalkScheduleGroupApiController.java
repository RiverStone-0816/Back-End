package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.schedule;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkMentResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleGroupListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkScheduleGroupSummaryResponse;
import kr.co.eicn.ippbx.model.enums.TalkScheduleKind;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkMentRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkScheduleGroupListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkScheduleGroupRepository;
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

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡일정관리 > 스케쥴유형
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/schedule/type", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkScheduleGroupApiController extends ApiBaseController {

	private final WtalkScheduleGroupRepository repository;
	private final WtalkScheduleGroupListRepository wtalkScheduleGroupListRepository;
	private final WtalkMentRepository wtalkMentRepository;

	/**
	 * 스케쥴유형 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<WtalkScheduleGroupSummaryResponse>>> list() {
		return ResponseEntity.ok(data(repository.getTalkScheduleGroupLists().stream().map(e -> convertDto(e, WtalkScheduleGroupSummaryResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 스케쥴유형 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody TalkScheduleGroupFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/wtalk/schedule/type")).body(create());
	}

	/**
	 * 스케쥴유형 삭제
	 */
	@DeleteMapping("{parent}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer parent) {
		repository.deleteOnIfNullThrow(parent);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴유형 항목 상세조회
	 */
	@GetMapping("item/{child}")
	public ResponseEntity<JsonResult<WtalkScheduleGroupListDetailResponse>> itemGet(@PathVariable Integer child) {
		WtalkScheduleGroupListDetailResponse entity = convertDto(wtalkScheduleGroupListRepository.findOneIfNullThrow(child), WtalkScheduleGroupListDetailResponse.class);
		if (entity.getKind().equals(TalkScheduleKind.CHAT_BOT_CONNECT.getCode()))
			entity.setChatBot(entity.getKindData());
		if (entity.getKind().equals(TalkScheduleKind.SERVICE_BY_GROUP_CONNECT.getCode()))
			entity.setTalkGroup(entity.getKindData());

		return ResponseEntity.ok(data(entity));
	}

	/**
	 * 스케쥴유형 항목추가
	 */
	@PostMapping("item")
	public ResponseEntity<JsonResult<Void>> itemRegister(@Valid @RequestBody TalkScheduleGroupListFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		wtalkScheduleGroupListRepository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/wtalk/schedule/type/item")).body(create());
	}

	/**
	 * 스케쥴유형 항목수정
	 */
	@PutMapping("item/{child}")
	public ResponseEntity<JsonResult<Void>> itemUpdate(@Valid @RequestBody TalkScheduleGroupListFormRequest form, BindingResult bindingResult, @PathVariable Integer child) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		wtalkScheduleGroupListRepository.updateByKey(form, child);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴유형 항목삭제
	 */
	@DeleteMapping("item/{child}")
	public ResponseEntity<JsonResult<Void>> itemDelete(@PathVariable Integer child) {
		wtalkScheduleGroupListRepository.deleteOnIfNullThrow(child);
		return ResponseEntity.ok(create());
	}

	@GetMapping("wtalk-ment")
	public ResponseEntity<JsonResult<List<SummaryWtalkMentResponse>>> getTalkMent() {
		return ResponseEntity.ok(data(wtalkMentRepository.findAll().stream().map(e -> convertDto(e, SummaryWtalkMentResponse.class)).collect(Collectors.toList())));
	}
}
