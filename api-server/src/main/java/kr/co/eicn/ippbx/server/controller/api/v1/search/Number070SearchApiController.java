package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchNumber070Response;
import kr.co.eicn.ippbx.model.search.search.SearchNumber070Request;
import kr.co.eicn.ippbx.server.repository.eicn.Number070Repository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/number", produces = MediaType.APPLICATION_JSON_VALUE)
public class Number070SearchApiController extends ApiBaseController {
	private final Number070Repository repository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<SearchNumber070Response>>> search(SearchNumber070Request search) {
		return ResponseEntity.ok(data(repository.search(search)));
	}
}
