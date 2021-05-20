package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMeta;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * 공통 조직정보 API
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/organization", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationCommonApiController extends ApiBaseController {

	private final OrganizationService organizationService;

	/**
	 * 그룹레벨메타정보, 해당 그룹레벨에 조직정보 목록을 반환
	 */
	@GetMapping("meta-tree")
	public ResponseEntity<JsonResult<List<OrganizationMeta>>> getMetaTree() {
		return ResponseEntity.ok(data(organizationService.getMetaTree()));
	}

	/**
	 * 조직정보 조회
	 */
	@GetMapping("{groupCode}")
	public ResponseEntity<JsonResult<OrganizationSummaryResponse>> getCompanyTreeGroupCode(@PathVariable String groupCode) {
		return ResponseEntity.ok(data(convertDto(organizationService.findOneCompanyTree(groupCode), OrganizationSummaryResponse.class)));
	}

	/**
	 * 조직정보 목록조회
	 *  - 차수별 조직정보 목록
	 */
	@GetMapping("{groupCode}/company-tree")
	public ResponseEntity<JsonResult<List<OrganizationSummaryResponse>>> getCompanyTreeGroupCodeList(@PathVariable String groupCode) {
		return ResponseEntity.ok(data(organizationService.getCompanyTrees(groupCode).stream()
				.map((e) -> convertDto(e, OrganizationSummaryResponse.class))
				.collect(Collectors.toList())));
	}

	@GetMapping("company-tree")
	public ResponseEntity<JsonResult<List<OrganizationSummaryResponse>>> companyTree(@RequestParam Integer groupLevel, @RequestParam(required = false) String parentGroupCode) {
		return ResponseEntity.ok(data(organizationService.getAllCompanyTrees().stream()
				.filter(e -> e.getGroupLevel().equals(groupLevel))
				.filter(e -> contains(e.getGroupTreeName(), defaultString(parentGroupCode)))
				.map((e) -> convertDto(e, OrganizationSummaryResponse.class))
				.collect(Collectors.toList()))
		);
	}
}
