package kr.co.eicn.ippbx.front.controller.api.record.evaluation;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationResultApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultStatResponse;
import kr.co.eicn.ippbx.server.model.form.DisputeEvaluationFormRequest;
import kr.co.eicn.ippbx.server.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.server.model.search.EvaluationResultSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/evaluation-result", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvaluationResultApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationResultApiController.class);

    private final EvaluationResultApiInterface apiInterface;

    @GetMapping("")
    public Pagination<EvaluationResultEntity> pagination(EvaluationResultSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{id}")
    public EvaluationResultEntity get(@PathVariable Integer id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    @GetMapping("stat")
    public List<EvaluationResultStatResponse> statistics(EvaluationResultSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.statistics(search);
    }

    /**
     * 평가결과통계 > 평가리스트 조회
     */
    @GetMapping("{targetUserId}/{evaluationId}")
    public List<EvaluationResultEntity> evaluations(@PathVariable String targetUserId, @PathVariable Long evaluationId) throws IOException, ResultFailException {
        return apiInterface.evaluations(targetUserId, evaluationId);
    }

    /**
     * 상담원평가
     */
    @PostMapping("")
    public void evaluationRegister(@Valid @RequestBody EvaluationResultFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.evaluationRegister(form);
    }

    @PutMapping("{id}/complete")
    public void complete(@Valid @RequestBody EvaluationResultFormRequest form, BindingResult bindingResult, @PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.complete(id, form);
    }

    @PutMapping("{id}/dispute")
    public void dispute(@Valid @RequestBody DisputeEvaluationFormRequest form, BindingResult bindingResult, @PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.dispute(id, form);
    }

    @PutMapping("{id}/confirm")
    public void confirm(@PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.confirm(id);
    }
}
