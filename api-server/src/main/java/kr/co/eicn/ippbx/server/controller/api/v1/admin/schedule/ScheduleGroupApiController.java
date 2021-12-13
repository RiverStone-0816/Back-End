package kr.co.eicn.ippbx.server.controller.api.v1.admin.schedule;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ConfRoom;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.model.form.ScheduleGroupFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListTimeUpdateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
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

import static java.util.Comparator.*;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.IvrTree.*;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.*;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.*;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 음원/IVR관리 > 일정관리 > [수신]스케쥴유형관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/sounds/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleGroupApiController extends ApiBaseController {

	private final ScheduleGroupRepository repository;
	private final ScheduleGroupListRepository scheduleGroupListRepository;
	private final SoundListRepository soundListRepository;
	private final Number070Repository numberRepository;
	private final ContextInfoRepository contextInfoRepository;
	private final IvrTreeRepository ivrTreeRepository;
	private final QueueNameRepository queueNameRepository;
	private final PhoneInfoRepository phoneInfoRepository;
	private final ServiceRepository serviceRepository;
	private final ConfRoomRepository confRoomRepository;

	/**
	 * 스케쥴유형 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<ScheduleGroupSummaryResponse>>> list() {
		return ResponseEntity.ok(data(repository.getScheduleGroupLists().stream().map(e -> convertDto(e, ScheduleGroupSummaryResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 스케쥴유형 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ScheduleGroupFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/sounds/schedule")).body(create());
	}

	/**
	 * 스케쥴유형 삭제
	 */
	@DeleteMapping("{parent}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer parent) {
		repository.deleteAllPBXServer(parent);
		scheduleGroupListRepository.deleteByParent(parent);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴유형 항목 상세조회
	 */
	@GetMapping("item/{child}")
	public ResponseEntity<JsonResult<ScheduleGroupListDetailResponse>> getItem(@PathVariable Integer child) {
		return ResponseEntity.ok(data(convertDto(scheduleGroupListRepository.findOneIfNullThrow(child), ScheduleGroupListDetailResponse.class)));
	}

	/**
	 * 스케쥴유형 항목추가
	 */
	@PostMapping("item")
	public ResponseEntity<JsonResult<Void>> registerItem(@Valid @RequestBody ScheduleGroupListFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		scheduleGroupListRepository.insertAllPbxServers(form);
		return ResponseEntity.created(URI.create("api/v1/admin/sounds/schedule/type/item")).body(create());
	}

	/**
	 * 스케쥴유형 항목수정
	 */
	@PutMapping("item/{child}")
	public ResponseEntity<JsonResult<Void>> updateItem(@Valid @RequestBody ScheduleGroupListFormRequest form, BindingResult bindingResult, @PathVariable Integer child) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		scheduleGroupListRepository.updateByKey(form, child);
		return ResponseEntity.ok(create());
	}

	@PutMapping("item/time/{child}")
	public ResponseEntity<JsonResult<Void>> updateTime(@Valid @RequestBody ScheduleGroupListTimeUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer child) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		scheduleGroupListRepository.updateTimeByKey(form, child);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴유형 항목삭제
	 */
	@DeleteMapping("item/{child}")
	public ResponseEntity<JsonResult<Void>> deleteItem(@PathVariable Integer child) {
		scheduleGroupListRepository.deleteByKey(child);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴유형 항목 복사
	 */
	@PostMapping("{parent}/{targetParent}/copy")
	public ResponseEntity<JsonResult<Void>> itemCopy(@PathVariable Integer parent, @PathVariable Integer targetParent) {
		repository.itemCopy(parent, targetParent);
		return ResponseEntity.ok(create());
	}

	/**
	 * 음원 목록 조회
	 */
	@GetMapping("add-sounds-list")
	public ResponseEntity<JsonResult<List<SummarySoundListResponse>>> addSoundList() {
		return ResponseEntity.ok(data(soundListRepository.findAll()
				.stream()
				.sorted(comparing(SoundList::getSoundName))
				.map(e -> convertDto(e, SummarySoundListResponse.class))
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 번호 목록 조회
	 */
	@GetMapping("add-number-list")
	public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> addNumber070List() {
		return ResponseEntity.ok(data(numberRepository.findAll()
			.stream()
			.sorted(comparing(Number_070::getType).reversed().thenComparing(Number_070::getNumber))
			.map(e -> {
				final SummaryNumber070Response response = convertDto(e, SummaryNumber070Response.class);
				if (NumberType.HUNT.getCode().equals(e.getType())) {
					queueNameRepository.findAll(QUEUE_NAME.NUMBER.eq(e.getNumber())).stream()
						.findFirst()
						.ifPresent(queue  -> response.setHanName(queue.getHanName()));
				} else if (NumberType.PERSONAL.getCode().equals(e.getType())) {
					phoneInfoRepository.findAll(PHONE_INFO.VOIP_TEL.eq(e.getNumber())).stream()
						.findFirst()
						.ifPresent(phone -> response.setHanName(phone.getExtension()));
				} else if (NumberType.SERVICE.getCode().equals(e.getType())) {
					serviceRepository.findAll(ServiceList.SERVICE_LIST.SVC_NUMBER.eq(e.getNumber())).stream()
							.findFirst()
							.ifPresent(service -> response.setHanName(service.getSvcName()));
				} else if (NumberType.CONFERENCE.getCode().equals(e.getType())) {
					confRoomRepository.findAll(ConfRoom.CONF_ROOM.ROOM_NUMBER.eq(e.getNumber())).stream()
							.findFirst()
							.ifPresent(room -> response.setHanName(room.getRoomName()));
				}

				return response;
			})
			.collect(Collectors.toList())));
	}

	/**
	 * Ivr 목록 조회
	 */
	@GetMapping("add-ivr-list")
	public ResponseEntity<JsonResult<List<SummaryIvrTreeResponse>>> addIvrTreeList() {
		return ResponseEntity.ok(data(ivrTreeRepository.findAll(IVR_TREE.TYPE.eq((byte) 1).and(IVR_TREE.LEVEL.eq(0)))
					.stream()
					.map( e -> convertDto(e, SummaryIvrTreeResponse.class))
					.collect(Collectors.toList()))
		);
	}

	/**
	 * context 목록 조회
	 */
	@GetMapping("add-context-list")
	public ResponseEntity<JsonResult<List<SummaryContextInfoResponse>>> addContextList() {
		return ResponseEntity.ok(data(contextInfoRepository.findAll().stream().map(e -> convertDto(e, SummaryContextInfoResponse.class)).collect(Collectors.toList())));
	}
}
