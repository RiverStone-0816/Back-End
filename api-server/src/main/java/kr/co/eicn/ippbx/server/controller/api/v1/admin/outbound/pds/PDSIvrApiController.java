package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSIvrDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSIvrResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryIvrTreeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPDSQueueNameResponse;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PDSIvrRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PDSQueueNameRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PdsIvr.PDS_IVR;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 *  아웃바운드관리 > PDS > IVR설정
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/ivr", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSIvrApiController extends ApiBaseController {

	private final PDSIvrRepository repository;
	private final PDSQueueNameRepository pdsQueueNameRepository;
	@Value("${file.path.ars}")
	private String savePath;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<PDSIvrResponse>>> list() {
		return ResponseEntity.ok(data(repository.getIvrLists().stream().map(e -> convertDto(e, PDSIvrResponse.class)).collect(Collectors.toList())));
	}

	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<PDSIvrDetailResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(repository.getIvr(seq)));
	}

	/**
	 * 루트IVR추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> postRoot(@Valid @RequestBody PDSIvrFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insertAllPbxServers(form);
		return ResponseEntity.created(URI.create("api/v1/admin/outbound/pds/ivr")).body(create());
	}

	/**
	 * PDS_TREE 수정
	 */
	@PutMapping("{code}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody PDSIvrFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer code) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKeyAllPbxServers(form, code);
		return ResponseEntity.ok(create());
	}

	/**
	 * 삭제
	 */
	@DeleteMapping("{code}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer code) {
		repository.deleteAllPbxServers(code);
		return ResponseEntity.ok(create());
	}

	@GetMapping("root-node")
	public ResponseEntity<JsonResult<List<SummaryIvrTreeResponse>>> rootNodes() {
		return ResponseEntity.ok(data(repository.findAll(PDS_IVR.TYPE.eq((byte) 0).and(PDS_IVR.LEVEL.eq(0))).stream().map(e -> convertDto(e, SummaryIvrTreeResponse.class)).collect(Collectors.toList())));
	}

	/**
	 * pds_queue_name 목록 조회
	 */
	@GetMapping("add-pds-queue")
	public ResponseEntity<JsonResult<List<SummaryPDSQueueNameResponse>>> addPdsQueueNames() {
		return ResponseEntity.ok(data(pdsQueueNameRepository.findAll()
				.stream()
				.map(e -> convertDto(e, SummaryPDSQueueNameResponse.class))
				.collect(Collectors.toList()))
		);
	}
}
