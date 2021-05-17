package kr.co.eicn.ippbx.server.controller.api.v1.admin.schedule.outbound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.OutScheduleList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.SoundList;
import kr.co.eicn.ippbx.server.model.dto.eicn.OutScheduleSeedDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.server.model.enums.ScheduleType;
import kr.co.eicn.ippbx.server.model.form.OutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.server.model.search.OutScheduleSeedSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.OutScheduleSeedRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SoundListRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * 음원/IVR관리 > 일정관리 > [발신]주간스케쥴러
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/sounds/schedule/outbound/week", produces = MediaType.APPLICATION_JSON_VALUE)
public class OutboundWeekScheduleSeedApiController extends ApiBaseController {

	private final OutScheduleSeedRepository repository;
	private final SoundListRepository soundListRepository;
	private final PhoneInfoRepository phoneInfoRepository;
	private final PersonListRepository personListRepository;

	/**
	 * 스케쥴러 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<OutScheduleSeedEntity>>> list(OutScheduleSeedSearchRequest search) {
		search.setType(ScheduleType.WEEK);
		return ResponseEntity.ok(data(repository.getScheduleLists(search).stream()
						.map((e) -> convertDto(e, OutScheduleSeedEntity.class))
						.collect(Collectors.toList()))
		);
	}

	/**
	 * 스케쥴러 상세조회
	 */
	@GetMapping("{parent}")
	public ResponseEntity<JsonResult<OutScheduleSeedDetailResponse>> get(@PathVariable Integer parent) {
		final OutScheduleSeedSearchRequest search = new OutScheduleSeedSearchRequest();
		search.setType(ScheduleType.WEEK);

		final List<SummaryPhoneInfoResponse> extensions = getExtensions();

		return ResponseEntity.ok(data(repository.getScheduleLists(search).stream()
				.filter(e -> e.getParent().equals(parent))
				.map(e -> {
					final OutScheduleSeedDetailResponse response = convertDto(e, OutScheduleSeedDetailResponse.class);

					response.setExtensions(extensions.stream()
							.filter(extension -> e.getOutScheduleLists().stream().map(OutScheduleList::getExtension).distinct().anyMatch(s -> s.equals(extension.getExtension())))
							.collect(Collectors.toList())
					);
					response.setWeeks(e.getOutScheduleLists().stream().map(OutScheduleList::getWeek).distinct().collect(Collectors.toList()));
					e.getOutScheduleLists().stream().findAny().ifPresent(scheduleList -> {
						response.setFromhour(scheduleList.getFromhour());
						response.setTohour(scheduleList.getTohour());
						response.setFromtime(scheduleList.getFromtime());
						response.setTotime(scheduleList.getTotime());
						response.setSoundCode(scheduleList.getSoundcode());
						response.setSoundName(scheduleList.getSoundName());
					});
					return response;
				})
				.findAny()
				.orElseThrow(() -> new EntityNotFoundException("스케쥴정보를 찾을 수 없습니다.")))
		);
	}

	/**
	 * 스케쥴러 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody OutScheduleSeedFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insertOnWeekScheduleAllPbxServers(form);
		return ResponseEntity.created(URI.create("api/v1/admin/sounds/schedule/outbound/week")).body(create());

	}

	/**
	 * 스케쥴러 수정
	 */
	@PutMapping("{parent}")
	public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody OutScheduleSeedFormRequest form, BindingResult bindingResult, @PathVariable Integer parent) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateAllPbxServers(form, parent);
		return ResponseEntity.ok(create());
	}

	/**
	 * 스케쥴러 삭제
	 */
	@DeleteMapping("{parent}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer parent) {
		repository.deleteAllPbxServers(parent);
		return ResponseEntity.ok(create());
	}

	/**
	 * 내선번호 목록조회
	 */
	@GetMapping("add-extensions")
	public ResponseEntity<JsonResult<List<SummaryPhoneInfoResponse>>> addExtensions() {
		return ResponseEntity.ok(data(getExtensions()));
	}

	/**
	 * 음원 목록 조회
	 */
	@GetMapping("add-sounds")
	public ResponseEntity<JsonResult<List<SummarySoundListResponse>>> addSounds() {
		return ResponseEntity.ok(data(soundListRepository.findAll()
				.stream()
				.sorted(comparing(SoundList::getSoundName))
				.map(e -> convertDto(e, SummarySoundListResponse.class))
				.collect(Collectors.toList()))
		);
	}

	private List<SummaryPhoneInfoResponse> getExtensions() {
		final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> useExtensionPersons = personListRepository.findAll(
				PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.ne(EMPTY)).and(PERSON_LIST.EXTENSION.ne("null")));

		return phoneInfoRepository.findAll(PHONE_INFO.EXTENSION.isNotNull().and(PHONE_INFO.EXTENSION.ne(EMPTY))).stream()
				.sorted(Comparator.comparing(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo::getExtension))
				.map(e -> {
					final SummaryPhoneInfoResponse response = convertDto(e, SummaryPhoneInfoResponse.class);
					useExtensionPersons.stream()
							.filter(person -> person.getExtension().equals(e.getExtension()))
							.findFirst()
							.ifPresent(person -> response.setInUseIdName(person.getIdName()));

					return response;
				})
				.collect(Collectors.toList());
	}
}
