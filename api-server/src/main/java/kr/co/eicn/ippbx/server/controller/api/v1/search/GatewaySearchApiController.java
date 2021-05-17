package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchGwInfoResponse;
import kr.co.eicn.ippbx.server.model.search.search.SearchGwInfoRequest;
import kr.co.eicn.ippbx.server.repository.eicn.GatewayRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/gateway", produces = MediaType.APPLICATION_JSON_VALUE)
public class GatewaySearchApiController {
	private final GatewayRepository repository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<SearchGwInfoResponse>>> search(SearchGwInfoRequest search) {
		return ResponseEntity.ok(data(repository.search(search)));
	}
}
