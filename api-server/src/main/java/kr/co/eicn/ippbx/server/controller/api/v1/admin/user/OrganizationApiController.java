package kr.co.eicn.ippbx.server.controller.api.v1.admin.user;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.model.form.CompanyTreeNameUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.OrganizationFormRequest;
import kr.co.eicn.ippbx.model.form.OrganizationFormUpdateRequest;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 번호/그룹/사용자 > 조직관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/organization", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationApiController extends ApiBaseController {
	private final OrganizationService organizationService;
	private final static String COMMEND = "/home/ippbxmng/lib/doub_orginfo_update.sh ";
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

		doubOrginFoUpdate();
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
		doubOrginFoUpdate();
		return ResponseEntity.ok(create());
	}

	/**
	 * 조직구성 삭제
	 */
	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		organizationService.deleteOrganization(seq);
		doubOrginFoUpdate();
		return ResponseEntity.ok(create());
	}

	@IsAdmin
	@GetMapping(value = "/meta-type")
	public ResponseEntity<JsonResult<List<CompanyTreeLevelNameResponse>>> listMetaType() {
		final List<CompanyTreeLevelNameResponse> rows = organizationService.listMetaType().stream()
				.map(e -> convertDto(e, CompanyTreeLevelNameResponse.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok(data(rows));
	}

	@GetMapping(value = "/meta-type/counsel")
	public ResponseEntity<JsonResult<List<CompanyTreeLevelNameResponse>>> listMetaTypeCounsel() {
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

		doubOrginFoUpdate();

		return ResponseEntity.ok(create());
	}

	private void doubOrginFoUpdate() {
		if (g.getUser().getCompany().getService().contains("DUTA") || g.getUser().getCompany().getService().contains("DUSTT")) {
			try {
				Runtime runtime = Runtime.getRuntime();
				Process process = runtime.exec(COMMEND + g.getUser().getCompanyId());
				process.waitFor();

				if (process.exitValue() == 0)
					log.info(COMMEND + " 호출성공");
				else
					log.info(COMMEND + " 호출실패");

			} catch (InterruptedException | IOException e) {
				log.error(String.valueOf(e));
			}
		}
	}
}
