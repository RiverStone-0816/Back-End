package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.type;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonBasicField;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKindGroup;
import kr.co.eicn.ippbx.server.model.form.CommonTypeFormRequest;
import kr.co.eicn.ippbx.server.model.form.CommonTypeUpdateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CommonBasicFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonTypeRepository;
import kr.co.eicn.ippbx.server.util.EnumUtils;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 상담어플리케이션관리 > 유형관리 > 고객DB유형, 연동DB유형, 상담결과유형
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/type", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonTypeApiController extends ApiBaseController {

	private final CommonTypeRepository repository;
	private final CommonFieldRepository commonFieldRepository;
	private final CommonBasicFieldRepository commonBasicFieldRepository;

	/**
	 *  유형관리 목록조회
	 */
	@GetMapping(value = "", params = "kind")
	public ResponseEntity<JsonResult<List<CommonTypeEntity>>> list(@RequestParam String kind) {
		if (Objects.isNull(CommonTypeKindGroup.findByCommonTypeKind(CommonTypeKindGroup.APPLICATION, EnumUtils.of(CommonTypeKind.class, kind))))
			throw new IllegalArgumentException(message.getText("messages.validator.invalid", kind));

		return ResponseEntity.ok(data(repository.getCommonTypeLists(kind)));
	}

	/**
	 * 유형 상세조회
	 */
	@GetMapping("{seq}")
	public ResponseEntity<JsonResult<CommonTypeEntity>> get(@PathVariable Integer seq) {
		return ResponseEntity.ok(data(repository.getCommonType(seq)));
	}

	/**
	 * 유형 추가
	 */
	@PostMapping("")
	public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody CommonTypeFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.insert(form);
		return ResponseEntity.created(URI.create("api/v1/admin/application/type")).body(create());
	}

	/**
	 * 유형 수정
	 */
	@PutMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody CommonTypeUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.update(form, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 유형 삭제
	 */
	@PatchMapping("{seq}")
	public ResponseEntity<JsonResult<Void>> updateStatus(@PathVariable Integer seq) {
		repository.updateStatus(seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 필드 순서 변경
	 */
	@PutMapping("{seq}/move")
	public ResponseEntity<JsonResult<Void>> move(@RequestParam List<Integer> sequences, @PathVariable Integer seq) {
		if (CollectionUtils.isEmpty(sequences))
			throw new IllegalArgumentException();

		commonFieldRepository.changeTheOrder(sequences, seq);
		return ResponseEntity.ok(create());
	}

	/**
	 * 유형 기본 필드 목록조회
	 */
	@GetMapping(value = "/basic-field")
	public ResponseEntity<JsonResult<List<CommonBasicField>>> getBasicFields(@RequestParam String kind) {
		return ResponseEntity.ok(data(commonBasicFieldRepository.findAllServiceKindAndInfo(kind)));
	}
}
