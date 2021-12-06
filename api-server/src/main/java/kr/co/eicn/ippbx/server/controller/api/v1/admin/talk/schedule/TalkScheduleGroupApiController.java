package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.schedule;

import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.enums.TalkScheduleKind;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkMentResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkScheduleGroupListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkScheduleGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.TalkMentRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkScheduleGroupListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TalkScheduleGroupRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/schedule/type", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkScheduleGroupApiController extends ApiBaseController {

	private final TalkScheduleGroupRepository repository;
	private final TalkScheduleGroupListRepository talkScheduleGroupListRepository;
	private final TalkMentRepository talkMentRepository;

	/**
	 * 스케쥴유형 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<TalkScheduleGroupSummaryResponse>>> list() {
		return ResponseEntity.ok(data(repository.getTalkScheduleGroupLists().stream().map(e -> convertDto(e, TalkScheduleGroupSummaryResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 스케쥴유형 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody TalkScheduleGroupFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/talk/schedule/type")).body(create());
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
	public ResponseEntity<JsonResult<TalkScheduleGroupListDetailResponse>> itemGet(@PathVariable Integer child) {
		TalkScheduleGroupListDetailResponse entity = convertDto(talkScheduleGroupListRepository.findOneIfNullThrow(child), TalkScheduleGroupListDetailResponse.class);
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

		talkScheduleGroupListRepository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/talk/schedule/type/item")).body(create());
	}

	/**
	 * 스케쥴유형 항목수정
	 */
	@PutMapping("item/{child}")
	public ResponseEntity<JsonResult<Void>> itemUpdate(@Valid @RequestBody TalkScheduleGroupListFormRequest form, BindingResult bindingResult, @PathVariable Integer child) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		talkScheduleGroupListRepository.updateByKey(form, child);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴유형 항목삭제
	 */
	@DeleteMapping("item/{child}")
	public ResponseEntity<JsonResult<Void>> itemDelete(@PathVariable Integer child) {
		talkScheduleGroupListRepository.deleteOnIfNullThrow(child);
		return ResponseEntity.ok(create());
	}

	@GetMapping("talk-ment")
	public ResponseEntity<JsonResult<List<SummaryTalkMentResponse>>> getTalkMent() {
		return ResponseEntity.ok(data(talkMentRepository.findAll().stream().map(e -> convertDto(e, SummaryTalkMentResponse.class)).collect(Collectors.toList())));
	}
}
