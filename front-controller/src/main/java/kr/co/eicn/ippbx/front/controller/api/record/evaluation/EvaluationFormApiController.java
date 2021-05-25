package kr.co.eicn.ippbx.front.controller.api.record.evaluation;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationFormApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationCategoryEntity;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationFormEntity;
import kr.co.eicn.ippbx.model.form.EvaluationFormRequest;
import kr.co.eicn.ippbx.model.form.EvaluationFormVisibleRequest;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.util.page.PageForm;
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
@RequestMapping(value = "api/evaluation-form", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvaluationFormApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationFormApiController.class);

    private final EvaluationFormApiInterface apiInterface;

    @GetMapping("")
    public Pagination<EvaluationForm> pagination(PageForm search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    /**
     * 해당 평가지에 항목정보를 호출
     */
    @GetMapping("{id}/category")
    public List<EvaluationCategoryEntity> getCategories(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.getCategories(id);
    }

    @GetMapping("{id}")
    public EvaluationFormEntity get(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @PostMapping("")
    public Long post(@Valid @RequestBody EvaluationFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{id}")
    public void put(@Valid @RequestBody EvaluationFormRequest form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    @PutMapping("{id}/hide")
    public void hide(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.hide(id);
    }

    @PutMapping("{id}/show")
    public void show(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.show(id);
    }

    /**
     * 평가지 숨김 처리
     */
    @PutMapping("hidden")
    public void hiddenUpdate(@Valid @RequestBody List<EvaluationFormVisibleRequest> ids, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.hiddenUpdate(ids);
    }

    /**
     * 평가지 복사
     *
     * @param id 복사할 평가지 아이디
     */
    @PostMapping("{id}/copy")
    public void copy(@Valid @RequestBody EvaluationFormRequest form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.copy(id, form);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    @GetMapping("search")
    public List<EvaluationForm> search(EvaluationFormSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.search(search);
    }
}
