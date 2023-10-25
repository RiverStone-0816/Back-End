package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchServiceResponse;
import kr.co.eicn.ippbx.model.search.PhoneSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ExtensionRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 대표서비스 조회
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceSearchApiController {
	private final ServiceRepository repository;
	private final ExtensionRepository extensionRepository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<SearchServiceResponse>>> search(SearchServiceRequest search) {
		return ResponseEntity.ok(data(repository.search(search)));
	}

	@GetMapping("outbound")
	public ResponseEntity<JsonResult<List<SearchServiceResponse>>> outboundServices(SearchServiceRequest search) {
		List<SearchServiceResponse> results = repository.search(search);
		results = results.stream().map(e -> {
			e.setSvcCid("대표_" + e.getSvcNumber());
			return e;
		}).collect(Collectors.toList());

		PhoneSearchRequest phoneSearch = new PhoneSearchRequest();
		final List<PhoneInfo> sipBuddies = extensionRepository.pagination(phoneSearch).getRows();

		if (sipBuddies.size() > 0) {
			for (PhoneInfo sipBudd : sipBuddies) {
				SearchServiceResponse serviceResponse = new SearchServiceResponse();

				serviceResponse.setSvcCid(sipBudd.getCid());
				serviceResponse.setSvcNumber(sipBudd.getVoipTel());
				serviceResponse.setSvcName(sipBudd.getExtension());

				results.add(serviceResponse);
			}
		}

		return ResponseEntity.ok(data(results));
	}
}
