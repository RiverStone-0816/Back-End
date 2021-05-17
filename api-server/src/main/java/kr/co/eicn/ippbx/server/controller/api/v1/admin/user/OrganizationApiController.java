package kr.co.eicn.ippbx.server.controller.api.v1.admin.user;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.server.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.server.model.form.OrganizationFormUpdateRequest;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 번호/그룹/사용자 > 조직관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/organization", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationApiController extends ApiBaseController {
	private final OrganizationService organizationService;

	/**
	 * 전체 조직구성 전달
	 * @return
	 */
	@GetMapping
	public ResponseEntity<JsonResult<List<Organization>>> getAllOrganizationPersons() {
		return ResponseEntity.ok(data(organizationService.getAllOrganizationPersons()));
	}

	/**
	 * 단일 조직구성 정보
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<Organization>> getOrganizationPerson(@PathVariable Integer seq) {
		return ResponseEntity.ok(
				data(organizationService.getAllOrganizationPersons().stream().filter(e -> Objects.equals(e.getSeq(), seq)).findAny().orElseThrow(EntityNotFoundException::new))
		);
	}

	/**
	 * 단일 조직구성의 설명: 계층 구조상의 전체 부모 구성원들 정보와 자식 구성원 및 사용자들의 숫자를 카운팅한다.
	 */
	@GetMapping("/{seq}/summary")
	public ResponseEntity<JsonResult<OrganizationPersonSummaryResponse>> getOrganizationPersonSummary(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(organizationService.getOrganizationPersonSummary(seq)));
	}

	/**
	 * 조직구성 등록
	 */
	@PostMapping
	public ResponseEntity<JsonResult<Integer>> register(@Valid @RequestBody OrganizationFormRequest form, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/user/organization")).body(data(organizationService.insertOrganization(form)));
	}

	/**
	 * 조직구성 수정
	 */
	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody OrganizationFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (bindingResult.hasErrors())
			throw new ValidationException(bindingResult);

		organizationService.updateOrganization(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 조직구성 삭제
	 */
	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		organizationService.deleteOrganization(seq);
		return ResponseEntity.ok(create());
	}

	@GetMapping(value = "/meta-type")
	public ResponseEntity<JsonResult<List<CompanyTreeLevelNameResponse>>> listMetaType() {
		final List<CompanyTreeLevelNameResponse> rows = organizationService.listMetaType().stream()
				.map(e -> convertDto(e, CompanyTreeLevelNameResponse.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(data(rows));
	}

	@PutMapping(value = "/meta-type")
	public ResponseEntity<JsonResult<Void>> updateMetaType(@Valid @RequestBody Set<CompanyTreeNameUpdateFormRequest> forms, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			throw new ValidationException(bindingResult);

		organizationService.updateMetaType(forms);
		return ResponseEntity.ok(create());
	}
}
