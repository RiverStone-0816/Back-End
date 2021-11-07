package kr.co.eicn.ippbx.front.controller.api.record.evaluation;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationItemApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItem;
import kr.co.eicn.ippbx.model.form.EvaluationWholeCategoryFormRequest;
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
@RequestMapping(value = "api/evaluation-item", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvaluationItemApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationItemApiController.class);

    private final EvaluationItemApiInterface apiInterface;

    @GetMapping("")
    public List<EvaluationItem> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{id}")
    public EvaluationItem get(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    /**
     * 평가항목 저장
     */
    @PostMapping("target/{evaluationId}")
    public void post(@Valid @RequestBody EvaluationWholeCategoryFormRequest form, BindingResult bindingResult, @PathVariable Long evaluationId) throws IOException, ResultFailException {
       apiInterface.post(evaluationId, form);
    }

    @GetMapping("search")
    public List<EvaluationItem> search() throws IOException, ResultFailException {
        return apiInterface.search();
    }
}
