package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.research;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ResearchListResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.server.model.form.ResearchListFormRequest;
import kr.co.eicn.ippbx.server.model.form.ResearchTreeFormRequest;
import kr.co.eicn.ippbx.server.model.search.ResearchListSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchTreeRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ResearchList.RESEARCH_LIST;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 아웃바운드관리 > 설문관리 > 설문관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/research", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResearchListApiController extends ApiBaseController {

	private final ResearchListRepository repository;
	private final ResearchTreeRepository researchTreeRepository;
	private final OrganizationService organizationService;

	@GetMapping("")
	public ResponseEntity<JsonResult<Pagination<ResearchListResponse>>> pagination(ResearchListSearchRequest search) {
		final Pagination<ResearchList> pagination = repository.pagination(search);
		final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

		final List<ResearchListResponse> rows = pagination.getRows().stream()
				.map((e) -> {
					final ResearchListResponse summary = convertDto(e, ResearchListResponse.class);
					if (isNotEmpty(summary.getGroupCode()))
						summary.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, summary.getGroupCode())
								.stream()
								.map(organization -> convertDto(organization, OrganizationSummaryResponse.class))
								.collect(Collectors.toList())
						);
					return summary;
				})
				.collect(Collectors.toList());
		return ResponseEntity.ok(data(new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit())));
	}

	@GetMapping("{researchId}")
	public ResponseEntity<JsonResult<ResearchListResponse>> get(@PathVariable Integer researchId) {
		final ResearchListResponse detail = convertDto(repository.findOneIfNullThrow(RESEARCH_LIST.RESEARCH_ID.eq(researchId)), ResearchListResponse.class);

		if (isNotEmpty(detail.getGroupCode()))
			detail.setCompanyTrees(organizationService.getCompanyTrees(detail.getGroupCode())
					.stream()
					.map(organization -> convertDto(organization, OrganizationSummaryResponse.class))
					.collect(Collectors.toList())
			);

		return ResponseEntity.ok(data(detail));
	}

	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> register(@Valid @RequestBody ResearchListFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("/api/v1/admin/outbound/research")).body(create());
	}

	@PutMapping("{researchId}")
	public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody ResearchListFormRequest form, BindingResult bindingResult, @PathVariable Integer researchId) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByResearchId(form, researchId);
		return ResponseEntity.ok(create());
	}

	@GetMapping("{researchId}/trees")
	public JsonResult<List<ResearchTree>> getTrees(@PathVariable Integer researchId) {
		return JsonResult.data(researchTreeRepository.findAllByResearchId(researchId));
	}

	@DeleteMapping("{researchId}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer researchId) {
		repository.delete(researchId);
		return ResponseEntity.ok(create());
	}

	@GetMapping("scenario/{researchId}")
	public ResponseEntity<JsonResult<ResearchListEntity>> getScenario(@PathVariable Integer researchId) {
		return ResponseEntity.ok(data(researchTreeRepository.getResearchScenarioLists(researchId)));
	}

	/**
	 * 설문시나리오설정
	 */
	@PostMapping("{researchId}/scenario")
	public ResponseEntity<JsonResult<Void>> scenarioRegister(@Valid @NotNull @RequestBody ResearchTreeFormRequest form, BindingResult bindingResult,
	                                                         @PathVariable Integer researchId) throws JsonProcessingException {
		if (bindingResult.hasErrors())
			throw new ValidationException(bindingResult);

		researchTreeRepository.insert(form, researchId);
		return ResponseEntity.ok(create());
	}
}
