package kr.co.eicn.ippbx.server.controller.api.v1.admin.schedule;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.HolyInfoResponse;
import kr.co.eicn.ippbx.model.form.HolyInfoFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.HolyInfoRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 일정관리 > 공휴일관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/schedule/holy", produces = MediaType.APPLICATION_JSON_VALUE)
public class HolyInfoApiController extends ApiBaseController {

	private final HolyInfoRepository repository;

	@GetMapping("")
	public ResponseEntity<JsonResult<List<HolyInfoResponse>>> list() {
		return ResponseEntity.ok(data(repository.findAll().stream().map(e -> convertDto(e, HolyInfoResponse.class)).collect(Collectors.toList())));
	}

	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<HolyInfoResponse>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(convertDto(repository.findOneIfNullThrow(seq), HolyInfoResponse.class)));
	}

	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody HolyInfoFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/schedule/holy")).body(create());
	}

	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody HolyInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKey(form, seq);
		return ResponseEntity.ok(create());
	}

	@DeleteMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
		repository.deleteOnIfNullThrow(seq);
		return ResponseEntity.ok(create());
	}
}
