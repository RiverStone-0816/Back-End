package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.enums.CommonTypeStatus;
import kr.co.eicn.ippbx.model.enums.ServerType;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.CommonFieldPoster;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonField.COMMON_FIELD;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsIvr.PDS_IVR;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 *  아웃바운드관리 > PDS > 그룹관리/실행요청
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSGroupApiController extends ApiBaseController {

	private final PDSGroupRepository repository;
	private final ResearchListRepository researchListRepository;
	private final PDSIvrRepository pdsIvrRepository;
	private final Number070Repository numberRepository;
	private final CommonTypeRepository commonTypeRepository;
	private final PDSQueueNameRepository pdsQueueNameRepository;
	private final CompanyServerRepository companyServerRepository;
	private final OrganizationService organizationService;
	private final CommonFieldRepository commonFieldRepository;
	private final CommonFieldPoster commonFieldPoster;

	@GetMapping("search")
	public ResponseEntity<JsonResult<List<PDSGroupSummaryResponse>>> list(PDSGroupSearchRequest search) {
		if (search.getStartDate() != null && search.getEndDate() != null)
			if (search.getStartDate().after(search.getEndDate()))
				throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));

		final List<PdsGroup> list = repository.findAll(search);
		final List<PDSGroupSummaryResponse> rows = list.stream()
				.map((e) -> convertDto(e, PDSGroupSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(rows));
	}

	@GetMapping("")
	public ResponseEntity<JsonResult<Pagination<PDSGroupSummaryResponse>>> pagination(PDSGroupSearchRequest search) {
		if (search.getStartDate() != null && search.getEndDate() != null)
			if (search.getStartDate().after(search.getEndDate()))
				throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));

		final Pagination<PdsGroup> pagination = repository.pagination(search);
		final List<PDSGroupSummaryResponse> rows = pagination.getRows().stream()
				.map((e) -> convertDto(e, PDSGroupSummaryResponse.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
	}

	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<PDSGroupDetailResponse>> get(@PathVariable Integer seq) {
		final PDSGroupDetailResponse detail = convertDto(repository.findOneIfNullThrow(seq), PDSGroupDetailResponse.class);

		final CommonType pdsType = commonTypeRepository.findOne(detail.getPdsType());

		if (pdsType != null)
			detail.setPdsTypeName(pdsType.getName());
		if (StringUtils.isNotEmpty(detail.getGroupCode()))
			detail.setCompanyTrees(organizationService.getCompanyTrees(detail.getGroupCode())
					.stream().map(e -> convertDto(e, OrganizationSummaryResponse.class))
					.collect(Collectors.toList())
			);

		return ResponseEntity.ok(data(detail));
	}

	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody PDSGroupFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insertWithPDSCustomInfoTableCreate(form);
		return ResponseEntity.created(URI.create("/api/v1/admin/outbound/pds/group")).body(create());
	}

	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody PDSGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKey(form, seq);
		return ResponseEntity.ok(create());
	}

	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		repository.deleteWithRelationShipTable(seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 실행요청
	 */
	@PostMapping("{seq}/execute")
	public ResponseEntity<JsonResult<Void>> executeRequest(@Valid @RequestBody PDSExecuteFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.executeRequest(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 유형 목록 조회
	 */
	@GetMapping(value = "add-common-type", params = "kind")
	public ResponseEntity<JsonResult<List<SummaryCommonTypeResponse>>> addCommonTypeLists(@RequestParam String kind) {
		final CommonTypeKind enumKind = EnumUtils.of(CommonTypeKind.class, kind);
		if (Objects.isNull(enumKind))
			throw new IllegalArgumentException();

		return ResponseEntity.ok(data(commonTypeRepository.findByKindStatus(enumKind, CommonTypeStatus.USING)
				.stream()
				.sorted(comparing(CommonType::getName))
				.map(e -> convertDto(e, SummaryCommonTypeResponse.class))
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 실행할교환기 목록 조회
	 */
	@GetMapping("add-server")
	public ResponseEntity<JsonResult<List<SummaryCompanyServerResponse>>> addServerLists() {
		return ResponseEntity.ok(data(companyServerRepository.findAllType(ServerType.PBX)
				.stream()
				.map(e -> {
					SummaryCompanyServerResponse response = convertDto(e, SummaryCompanyServerResponse.class);
					response.setName(e.getServer().getName());
					return response;
				})
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 과금번호설정 목록 조회
	 */
	@GetMapping("add-numbers")
	public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> addNumberLists() {
		return ResponseEntity.ok(data(numberRepository.findAll(NUMBER_070.NUMBER.like("070%"))
				.stream()
				.map(e -> convertDto(e, SummaryNumber070Response.class))
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 상담원그룹 연결대상 목록 조회(실행할교환기선택후사용가능)
	 */
	@GetMapping("add-pds-queue")
	public ResponseEntity<JsonResult<List<SummaryPDSQueueNameResponse>>> addPDSQueueNameLists() {
		return ResponseEntity.ok(data(pdsQueueNameRepository.findAll()
				.stream()
				.map(e -> convertDto(e, SummaryPDSQueueNameResponse.class))
				.collect(Collectors.toList()))
		);
	}

	/**
	 *  PDS IVR 목록 조회
	 */
	@GetMapping("add-pds-ivr")
	public ResponseEntity<JsonResult<List<SummaryIvrTreeResponse>>> addPDSIvrLists() {
		return ResponseEntity.ok(data(pdsIvrRepository.findAll(PDS_IVR.PARENT.eq(0))
				.stream()
				.map(e -> convertDto(e, SummaryIvrTreeResponse.class))
				.sorted(comparing(SummaryIvrTreeResponse::getName))
				.collect(Collectors.toList()))
		);
	}

	/**
	 *  상담결과유형 목록조회
	 */
	@GetMapping("add-consultation-result")
	public ResponseEntity<JsonResult<List<SummaryCommonTypeResponse>>> addConsultationResult() {
		return ResponseEntity.ok(data(commonTypeRepository.findByKindStatus(CommonTypeKind.CONSULTATION_RESULTS, CommonTypeStatus.USING)
				.stream()
				.map(e -> convertDto(e, SummaryCommonTypeResponse.class))
				.collect(Collectors.toList()))
		);
	}

	/**
	 *  설문 목록 조회
	 */
	@GetMapping("add-research")
	public ResponseEntity<JsonResult<List<SummaryResearchListResponse>>> addResearchLists() {
		return ResponseEntity.ok(data(researchListRepository.findAll()
				.stream()
				.map(e -> convertDto(e, SummaryResearchListResponse.class))
				.sorted(comparing(SummaryResearchListResponse::getResearchName))
				.collect(Collectors.toList()))
		);
	}

	/**
	 * 유형필드 목록 조회
	 */
	@GetMapping("add-field")
	public ResponseEntity<JsonResult<List<SummaryCommonFieldResponse>>> addCommonFieldLists() {
		return ResponseEntity.ok(data(commonFieldRepository.findAll(COMMON_FIELD.FIELD_TYPE.eq("NUMBER"))
				.stream()
				.sorted(Comparator.comparingInt(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField::getDisplaySeq))
				.map(e -> convertDto(e, SummaryCommonFieldResponse.class))
				.collect(Collectors.toList()))
		);
	}

	@PostMapping(value = "{seq}/fields/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@PathVariable Integer seq, @RequestParam MultipartFile file) throws IOException {
		commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.PDS, seq, file);
		return ResponseEntity.ok(create());
	}
}
