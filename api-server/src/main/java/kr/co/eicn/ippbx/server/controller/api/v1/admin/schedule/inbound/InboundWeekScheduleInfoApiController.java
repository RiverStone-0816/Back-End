package kr.co.eicn.ippbx.server.controller.api.v1.admin.schedule.inbound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ConfRoom;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleGroup;
import kr.co.eicn.ippbx.model.dto.eicn.Number070ScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.Number070ScheduleInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryScheduleGroupResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.form.ScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ScheduleInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.ScheduleInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.util.FunctionUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.QUEUE_NAME;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 음원/IVR관리 > 일정관리 > [수신]주간스케쥴러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/sounds/schedule/inbound/week", produces = MediaType.APPLICATION_JSON_VALUE)
public class InboundWeekScheduleInfoApiController extends ApiBaseController {

	private final ScheduleInfoRepository repository;
	private final Number070Repository numberRepository;
	private final ScheduleGroupRepository scheduleGroupRepository;
	private final QueueNameRepository queueNameRepository;
	private final PhoneInfoRepository phoneInfoRepository;
	private final ServiceRepository serviceRepository;
	private final ConfRoomRepository confRoomRepository;

	/**
	 * 주간스케쥴러 목록조회
	 */
	@IsAdmin
	@GetMapping("")
	public ResponseEntity<JsonResult<List<Number070ScheduleInfoResponse>>> list(ScheduleInfoSearchRequest search) {
		final Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList> serviceListMap =
				serviceRepository.findAll().stream().filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList::getSvcNumber))
						.collect(Collectors.toConcurrentMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList::getSvcNumber, e -> e));

		search.setType(ScheduleType.WEEK);

		return ResponseEntity.ok(data(numberRepository.getScheduleLists(search).stream()
						.map((e) -> {
							final Number070ScheduleInfoResponse response = convertDto(e, Number070ScheduleInfoResponse.class);
							if (NumberType.SERVICE.getCode().equals(response.getType())) {
								if (Objects.nonNull(serviceListMap.get(response.getNumber()))) {
									final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList serviceList = serviceListMap.get(response.getNumber());
									response.setSvcName(serviceList.getSvcName());
									response.setSvcCid(serviceList.getSvcCid());
								}
							}

							return response;
						})
						.collect(Collectors.toList()))
		);
	}

	/**
	 * 주간스케쥴러 유형보기
	 */
	@GetMapping("service/type/{parent}")
	public ResponseEntity<JsonResult<ScheduleGroupEntity>> getType(@PathVariable Integer parent) {
		return ResponseEntity.ok(data(scheduleGroupRepository.getScheduleGroupLists().stream()
						.filter(e -> e.getParent().equals(parent))
						.findFirst()
						.orElseThrow(() -> new EntityNotFoundException("스케쥴 유형정보를 찾을 수 없습니다."))));
	}

	/**
	 * 주간스케쥴러 일정 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<Number070ScheduleInfoDetailResponse>> get(@PathVariable Integer seq) {
		final ScheduleInfoSearchRequest search = new ScheduleInfoSearchRequest();
		search.setType(ScheduleType.WEEK);

		return ResponseEntity.ok(data(numberRepository.getScheduleLists(search).stream()
				.flatMap(e -> e.getScheduleInfos().stream())
				.filter(e -> Objects.equals(seq, e.getSeq()))
				.map(e -> {
					final Number070ScheduleInfoDetailResponse response = new Number070ScheduleInfoDetailResponse();
					response.setNumber(e.getNumber());
					response.setScheduleInfo(e);
					return response;
				})
				.findAny()
				.orElseThrow(() -> new EntityNotFoundException("스케쥴정보를 찾을 수 없습니다.")))
		);
	}

	/**
	 * 주간스케쥴러 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ScheduleInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insertOnWeekSchedule(form);
		return ResponseEntity.created(URI.create("api/v1/admin/schedule/week")).body(create());
	}

	/**
	 * 주간스케쥴러 삭제
	 */
	@DeleteMapping("{number}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable String number) {
		repository.deleteByNumber(ScheduleType.WEEK, number);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴일정 수정
	 */
	@PutMapping("service/type/{seq}")
	public ResponseEntity<JsonResult<Void>> updateType(@Valid @RequestBody ScheduleInfoUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByWeekType(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 검색
	 *  - 스케쥴러에 설정된 번호만 검색
	 */
	@GetMapping("/search-number-list")
	public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> searchNumbers() {
		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleInfo> scheduleInfos = repository.findAll(ScheduleInfo.SCHEDULE_INFO.TYPE.eq(ScheduleType.WEEK.getCode()));
		return ResponseEntity.ok(data(getNumbers().stream()
				.filter(e -> scheduleInfos.stream().map(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ScheduleInfo::getNumber).distinct().anyMatch(s -> s.equals(e.getNumber())))
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 주간스케쥴러 스케쥴유형 목록조회
	 */
	@GetMapping("/schedule-group")
	public ResponseEntity<JsonResult<List<SummaryScheduleGroupResponse>>> scheduleGroups() {
		return ResponseEntity.ok(data(scheduleGroupRepository.findAll().stream()
				.sorted(Comparator.comparing(ScheduleGroup::getName))
				.map(e -> convertDto(e, SummaryScheduleGroupResponse.class))
				.collect(Collectors.toList())));
	}

	/**
	 * 추가 가능한 번호 목록 조회
	 */
	@GetMapping("add-number-list")
	public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> addNumber070List() {
		return ResponseEntity.ok(data(getNumbers()));
	}

	private List<SummaryNumber070Response> getNumbers() {
		return numberRepository.findAll()
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
				.collect(Collectors.toList());
	}
}
