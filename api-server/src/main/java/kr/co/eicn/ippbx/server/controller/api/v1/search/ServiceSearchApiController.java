package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.model.dto.eicn.search.SearchOutboundNumberResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchServiceResponse;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 대표서비스 조회
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceSearchApiController {
	private final ServiceRepository   serviceRepository;
	private final PhoneInfoRepository phoneInfoRepository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<SearchServiceResponse>>> search(SearchServiceRequest search) {
		return ResponseEntity.ok(data(serviceRepository.search(search)));
	}

	@GetMapping("outbound")
	public ResponseEntity<JsonResult<List<SearchOutboundNumberResponse>>> outboundServices(SearchServiceRequest search) {
		final List<SearchOutboundNumberResponse> rows = new ArrayList<>();
		serviceRepository.search(search).forEach(e -> {
			final SearchOutboundNumberResponse row = new SearchOutboundNumberResponse();
			row.setName("[대표번호] " + e.getSvcName());
			row.setNumber(e.getSvcCid());

			rows.add(row);
		});

		phoneInfoRepository.findAll().forEach(e -> {
			final SearchOutboundNumberResponse row = new SearchOutboundNumberResponse();
			row.setName("[개인번호] " + e.getVoipTel() + " (" + e.getExtension() + ")");
			row.setNumber(e.getVoipTel());

			rows.add(row);
		});

		return ResponseEntity.ok(data(rows));
	}
}
