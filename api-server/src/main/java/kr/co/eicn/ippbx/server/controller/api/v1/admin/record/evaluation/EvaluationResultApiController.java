package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.evaluation;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultStatResponse;
import kr.co.eicn.ippbx.server.model.form.DisputeEvaluationFormRequest;
import kr.co.eicn.ippbx.server.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.server.model.search.EvaluationResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EvaluationResultRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 녹취관리 > 평가결과
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Setter
@RequestMapping(value = "api/v1/admin/record/evaluation/result", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvaluationResultApiController extends ApiBaseController {

    private final EvaluationResultRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<EvaluationResultEntity>>> pagination(EvaluationResultSearchRequest search) {
        return ResponseEntity.ok(data(repository.pagination(search)));
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonResult<EvaluationResultEntity>> get(@PathVariable Integer id) {
        return ResponseEntity.ok(data(repository.findOneIfNullThrow(id)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer id) {
        repository.delete(id);
        return ResponseEntity.ok(create());
    }

    /**
     * 평가결과통계
     */
    @GetMapping("stat")
    public ResponseEntity<JsonResult<List<EvaluationResultStatResponse>>> statistics(EvaluationResultSearchRequest search) {
        return ResponseEntity.ok(data(repository.statistics(search)));
    }

    /**
     * 평가결과통계 > 평가리스트 조회
     */
    @GetMapping("{targetUserId}/{evaluationId}")
    public ResponseEntity<JsonResult<List<EvaluationResultEntity>>> evaluations(@PathVariable String targetUserId, @PathVariable Long evaluationId) {
        return ResponseEntity.ok(data(repository.findAllByTargetUserIdAndEvaluationId(targetUserId, evaluationId)));
    }

    /**
     * 상담원평가
     */
    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> evaluationRegister(@Valid @RequestBody EvaluationResultFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.evaluationRegister(form);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{id}/complete")
    public ResponseEntity<JsonResult<Void>> complete(@Valid @RequestBody EvaluationResultFormRequest form, BindingResult bindingResult, @PathVariable Integer id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.complete(id, form);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{id}/dispute")
    public ResponseEntity<JsonResult<Void>> dispute(@Valid @RequestBody DisputeEvaluationFormRequest form, BindingResult bindingResult, @PathVariable Integer id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.dispute(id, form);
        return ResponseEntity.ok(create());
    }

    @PutMapping("{id}/confirm")
    public ResponseEntity<JsonResult<Void>> confirm(@PathVariable Integer id) {
        repository.confirm(id);
        return ResponseEntity.ok(create());
    }
}
