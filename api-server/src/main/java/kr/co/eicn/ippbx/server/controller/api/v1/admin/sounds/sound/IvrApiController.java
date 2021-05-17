package kr.co.eicn.ippbx.server.controller.api.v1.admin.sounds.sound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.ConfRoom;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.ServiceList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.enums.NumberType;
import kr.co.eicn.ippbx.server.model.form.IvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.IvrFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.IvrPositionFormRequest;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DSL;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.IvrTree.IVR_TREE;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.QueueName.QUEUE_NAME;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 *  음원/IVR관리 > IVR관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/sounds/ivr", produces = MediaType.APPLICATION_JSON_VALUE)
public class IvrApiController extends ApiBaseController {

	private final IvrTreeRepository repository;
	private final Number070Repository number070Repository;
	private final QueueNameRepository queueNameRepository;
	private final PhoneInfoRepository phoneInfoRepository;
	private final ServiceRepository serviceRepository;
	private final ConfRoomRepository confRoomRepository;
	private final WebVoiceInfoRepository webVoiceInfoRepository;
	private final WebVoiceItemsRepository webVoiceItemsRepository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<IvrResponse>>> list() {
		return ResponseEntity.ok(data(repository.getIvrTreeLists().stream().map(e -> convertDto(e, IvrResponse.class)).collect(Collectors.toList())));
	}

	@GetMapping("root-ivr-trees")
	public ResponseEntity<JsonResult<List<IvrResponse>>> rootIvrTrees() {
		return ResponseEntity.ok(data(repository.getRootIvrTrees().stream().map(e -> convertDto(e, IvrResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<IvrResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(convertDto(repository.getIvr(seq), IvrResponse.class)));
	}

	/**
	 * 메뉴 등록
	 * 새로운 메뉴 연결이 필요할 경우 해당 api 메서드를 호출
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Integer>> postMenu(@Valid @RequestBody IvrFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		final Integer seq = repository.insertMenu(form);

		if (form.getCopyToSourceId() != null)
			repository.copy(form.getCopyToSourceId(), seq);

		return ResponseEntity.ok(data(seq));
	}

	/**
	 * IVR TREE 수정
	 */
	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody IvrFormUpdateRequest form, BindingResult bindingResult,
															   @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKeyAllPbxServers(form, seq);
		return ResponseEntity.ok(create());
	}

	@PutMapping("{seq}/position")
	public ResponseEntity<JsonResult<Void>> updatePosition(@Valid @RequestBody IvrPositionFormRequest form, BindingResult bindingResult,
												@PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updatePosition(seq, form);
		return ResponseEntity.ok(create());
	}

	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		repository.deleteAllPbxServers(seq);
		return ResponseEntity.ok(create());
	}

	@GetMapping("root-node")
	public ResponseEntity<JsonResult<List<SummaryIvrTreeResponse>>> rootNodes() {
		return ResponseEntity.ok(data(repository.findAll(IVR_TREE.TYPE.eq((byte) 1).and(IVR_TREE.LEVEL.eq(0)).and(IVR_TREE.BUTTON.ne(EMPTY))).stream().map(e -> convertDto(e, SummaryIvrTreeResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * @param sourceId 복사할 아이디
	 * @param targetId 복사대상 아이디
	 * @return
	 */
	@PostMapping("{sourceId}/{targetId}/copy")
	public ResponseEntity<JsonResult<Void>> copy(@PathVariable Integer sourceId, @PathVariable Integer targetId) {
		repository.copy(sourceId, targetId);
		return ResponseEntity.ok(create());
	}

	/**
	 * 보이는 ARS
	 **/
	@GetMapping("{ivrCode}/web-voice")
	public JsonResult<WebVoiceResponse> getWebVoice(@PathVariable Integer ivrCode) {
		final WebVoiceResponse entity = convertDto(webVoiceInfoRepository.findOneByIvrCode(ivrCode), WebVoiceResponse.class);
		entity.setItems(
				webVoiceItemsRepository.findAllByIvrCode(entity.getIvrCode()).stream()
						.map(f -> convertDto(f, WebVoiceItemsResponse.class))
						.collect(Collectors.toList())
		);

		return data(entity);
	}

	/**
	 * 보이는 ARS 적용
	 **/
	@PostMapping("{ivrCode}/apply")
	public ResponseEntity<JsonResult<Void>> apply(@PathVariable Integer ivrCode,
												  @Valid @RequestBody WebVoiceItemsFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		webVoiceInfoRepository.updateIvrCode(ivrCode, form);

		// TODO: 이슈! WEB_VOICE 서버 필요

//        final WebvoiceInfo info = webVoiceInfoRepository.findOneByContext(context);
//        final String serviceKey = companyInfoRepository.findServiceKey();
//        String result = "";
//
//        if (info != null)
//            result = webVoiceInfoRepository.apply(serviceKey,info,context);

		return ResponseEntity.ok(create());
	}

	/**
	 * Ivr 목록 조회
	 */
	@GetMapping("add-ivr-list")
	public ResponseEntity<JsonResult<List<IvrTree>>> addIvrTreeList() {
		return ResponseEntity.ok(data(repository.findAll()));
	}

	@GetMapping("ivr-list")
	public ResponseEntity<JsonResult<List<IvrTree>>> getIvrList() {
		return ResponseEntity.ok(data(repository.getIvrTreeList()));
	}

	/**
	 * 번호 목록 조회
	 */
	@GetMapping("add-number-list")
	public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> addNumber070List() {
		return ResponseEntity.ok(data(
				number070Repository.findAll(DSL.and(NUMBER_070.STATUS.eq((byte) 1).and(NUMBER_070.TYPE.ne((byte) 2))).or(NUMBER_070.TYPE.eq((byte) 2)))
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
					} else {
						response.setHanName("사용안함");
					}

					return response;
				})
				.collect(Collectors.toList())));
	}
}
