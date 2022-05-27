package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.schedule;

import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServiceInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatServiceInfo;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.form.DayTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.HolyTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatServiceInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkScheduleGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkScheduleInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WtalkServiceInfoRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.service.WtalkScheduleService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡일정관리 > 일별스케쥴러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/schedule/day", produces = MediaType.APPLICATION_JSON_VALUE)
public class DayWtalkScheduleInfoApiController extends ApiBaseController {

	private final WtalkScheduleInfoRepository repository;
	private final WtalkServiceInfoRepository wtalkServiceInfoRepository;
	private final WebchatServiceInfoRepository webchatServiceInfoRepository;
	private final WtalkScheduleGroupRepository wtalkScheduleGroupRepository;
	private final OrganizationService organizationService;
	private final WtalkScheduleService wtalkScheduleService;

	/**
	 * 상담톡 일별스케쥴러 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<WtalkServiceInfoResponse>>> list(TalkServiceInfoSearchRequest search) {
		search.setType(ScheduleType.DAY);
		return ResponseEntity.ok(data(wtalkScheduleService.list(search)));
	}

	/**
	 * 상담톡 일별스케쥴러 유형보기
	 */
	@GetMapping("service/type/{parent}")
	public ResponseEntity<JsonResult<WtalkScheduleGroupEntity>> getType(@PathVariable Integer parent) {
		return ResponseEntity.ok(data(wtalkScheduleGroupRepository.getTalkScheduleGroupLists().stream()
				.filter(e -> e.getParent().equals(parent))
				.findFirst()
				.orElseThrow(() -> new EntityNotFoundException("스케쥴 유형정보를 찾을 수 없습니다."))));
	}

	/**
	 * 상담톡 일별스케쥴러 유형 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<WtalkScheduleInfoDetailResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(repository.getTalkScheduleInfoLists().stream()
				.filter(e -> e.getSeq().equals(seq))
				.map(e -> {
					final WtalkScheduleInfoDetailResponse response = convertDto(e, WtalkScheduleInfoDetailResponse.class);
					if (TalkChannelType.KAKAO.getCode().equals(e.getChannelType())) {
						final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo entity = wtalkServiceInfoRepository.findOne(WtalkServiceInfo.WTALK_SERVICE_INFO.SENDER_KEY.eq(e.getSenderKey()));
						if (entity != null) {
							response.setKakaoServiceName(entity.getKakaoServiceName());
							response.setSenderKey(entity.getSenderKey());
							response.setIsChattEnable(entity.getIsChattEnable());
						}
					} else {
						final WebchatServiceInfo entity = webchatServiceInfoRepository.getBySenderKey(e.getSenderKey());
						if (entity != null) {
							response.setKakaoServiceName(entity.getWebchatServiceName());
							response.setSenderKey(entity.getSenderKey());
							response.setIsChattEnable(entity.getIsChattEnable());
						}
					}
					if (e.getGroupId() != null)
						response.setScheduleGroup(wtalkScheduleGroupRepository.getTalkScheduleGroupLists()
								.stream()
								.filter(group -> group.getParent().equals(e.getGroupId()))
								.findFirst()
								.orElse(null));
					if (StringUtils.isNotEmpty(e.getGroupCode()))
						response.setCompanyTrees(organizationService.getCompanyTrees(e.getGroupCode())
								.stream()
								.map(group -> modelMapper.map(group, OrganizationSummaryResponse.class))
								.collect(Collectors.toList()));

					return response;
				})
				.findAny()
				.orElseThrow(() -> new EntityNotFoundException("스케쥴정보를 찾을 수 없습니다.")))
		);
	}

	/**
	 * 상담톡 일별스케쥴러 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody DayTalkScheduleInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insertOnDaySchedule(form);
		return ResponseEntity.created(URI.create("api/v1/admin/wtalk/schedule/day")).body(create());

	}

	/**
	 * 상담톡 일별스케쥴러 삭제
	 */
	@DeleteMapping("{senderKey}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable String senderKey) {
		repository.deleteBySenderKey(ScheduleType.DAY.getCode(), senderKey);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡 일별스케쥴러 스케쥴유형 수정
	 */
	@PutMapping("service/type/{seq}")
	public ResponseEntity<JsonResult<Void>> updateType(@Valid @RequestBody TalkScheduleInfoFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKey(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡 일별스케쥴러 유형삭제
	 */
	@DeleteMapping("service/type/{seq}")
	public ResponseEntity<JsonResult<Void>> deleteType(@PathVariable Integer seq) {
		repository.deleteOnIfNullThrow(seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 상담톡 일별스케쥴러 스케쥴유형 목록조회
	 */
	@GetMapping("/schedule-info")
	public ResponseEntity<JsonResult<List<SummaryWtalkScheduleInfoResponse>>> scheduleInfos() {
		return ResponseEntity.ok(data(wtalkScheduleGroupRepository.findAll().stream()
				.map(e -> convertDto(e, SummaryWtalkScheduleInfoResponse.class))
				.sorted(Comparator.comparing(SummaryWtalkScheduleInfoResponse::getName))
				.collect(Collectors.toList())));
	}

	/**
	 * 관련상담톡서비스 목록조회
	 */
	@GetMapping("add-services")
	public ResponseEntity<JsonResult<List<SummaryWtalkServiceResponse>>> talkServices() {
		return ResponseEntity.ok(data(wtalkServiceInfoRepository.findAll().stream().map(e -> convertDto(e, SummaryWtalkServiceResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * 공휴일 일괄등록
	 */
	@PostMapping("holy")
	public ResponseEntity<JsonResult<Void>> holyPost(@Valid @RequestBody HolyTalkScheduleInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insertOnDayHolySchedule(form);
		return ResponseEntity.created(URI.create("api/v1/admin/wtalk/schedule/day/holy")).body(create());
	}
}
