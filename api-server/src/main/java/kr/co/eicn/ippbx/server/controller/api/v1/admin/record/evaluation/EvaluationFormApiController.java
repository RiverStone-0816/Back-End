package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.evaluation;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.model.form.EvaluationFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationFormVisibleRequest;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EvaluationCategoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.EvaluationFormRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationForm.EVALUATION_FORM;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 녹취관리 > 평가지관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/evaluation/form", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvaluationFormApiController extends ApiBaseController {

	private final EvaluationFormRepository repository;
	private final EvaluationCategoryRepository evaluationCategoryRepository;

	@GetMapping("")
	public ResponseEntity<JsonResult<Pagination<EvaluationForm>>> pagination(PageForm search) {
		return ResponseEntity.ok(data(repository.pagination(search)));
	}

	/**
	 *  해당 평가지에 항목정보를 호출
	 */
	@GetMapping("{id}/category")
	public ResponseEntity<JsonResult<List<EvaluationCategoryEntity>>> getCategories(@PathVariable Long id) {
		return ResponseEntity.ok(data(evaluationCategoryRepository.getCategories(id)));
	}

	@GetMapping("{id}")
	public ResponseEntity<JsonResult<EvaluationFormEntity>> get(@PathVariable Long id) {
		return ResponseEntity.ok(data(repository.get(id)));
	}

	@PostMapping("")
	public ResponseEntity<JsonResult<Long>> post(@Valid @RequestBody EvaluationFormRequest form, BindingResult bindingResult) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		return ResponseEntity.created(URI.create("api/v1/admin/record/evaluation/form"))
				.body(data(repository.insertOnGeneratedKey(form).getValue(EVALUATION_FORM.ID)));
	}

	@PutMapping("{id}")
	public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody EvaluationFormRequest form, BindingResult bindingResult, @PathVariable Long id) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.updateByKey(form, id);
		return ResponseEntity.ok(create());
	}

	/**
	 *  평가지 숨김 처리
	 */
	@PutMapping("hidden")
	public ResponseEntity<JsonResult<Void>> hiddenUpdate(@Valid @RequestBody List<EvaluationFormVisibleRequest> ids, BindingResult bindingResult) {
		for (EvaluationFormVisibleRequest form: ids) {
			if (!form.validate(bindingResult))
				throw new ValidationException(bindingResult);
		}

		repository.hiddenUpdate(ids);
		return ResponseEntity.ok(create());
	}

	@PutMapping("{id}/hide")
	public ResponseEntity<JsonResult<Void>> hide(@PathVariable Long id)  {
		repository.hide(id);
		return ResponseEntity.ok(create());
	}

	@PutMapping("{id}/show")
	public ResponseEntity<JsonResult<Void>> show(@PathVariable Long id) {
		repository.show(id);
		return ResponseEntity.ok(create());
	}

	/**
	 *  평가지 복사
	 * @param id 복사할 평가지 아이디
	 */
	@PostMapping("{id}/copy")
	public ResponseEntity<JsonResult<Void>> copy(@Valid @RequestBody EvaluationFormRequest form, BindingResult bindingResult, @PathVariable Long id) {
		if (!form.validate(bindingResult))
			throw new ValidationException(bindingResult);

		repository.copy(form, id);
		return ResponseEntity.ok(create());
	}

	@DeleteMapping("{id}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable Long id) {

		repository.delete(id);
		return ResponseEntity.ok(create());
	}

	@GetMapping("search")
	public ResponseEntity<JsonResult<List<EvaluationForm>>> search(EvaluationFormSearchRequest search) {
		return ResponseEntity.ok(data(repository.findAll(search)));
	}
}
