package kr.co.eicn.ippbx.front.controller.api.outbound.research;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.model.form.ResearchListFormRequest;
import kr.co.eicn.ippbx.model.form.ResearchTreeFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchListSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/research", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResearchApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ResearchApiController.class);

    private final ResearchApiInterface apiInterface;

    @GetMapping("")
    public Pagination<ResearchListResponse> pagination(ResearchListSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{researchId}")
    public ResearchListResponse get(@PathVariable Integer researchId) throws IOException, ResultFailException {
        return apiInterface.get(researchId);
    }

    @PostMapping("")
    public void register(@Valid @RequestBody ResearchListFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.register(form);
    }

    @PutMapping("{researchId}")
    public void update(@Valid @RequestBody ResearchListFormRequest form, BindingResult bindingResult, @PathVariable Integer researchId) throws IOException, ResultFailException {
        apiInterface.update(researchId, form);
    }

    @DeleteMapping("{researchId}")
    public void delete(@PathVariable Integer researchId) throws IOException, ResultFailException {
        apiInterface.delete(researchId);
    }

    @GetMapping("scenario/{researchId}")
    public ResearchListEntity getScenario(@PathVariable Integer researchId) throws IOException, ResultFailException {
        return apiInterface.getScenario(researchId);
    }

    /**
     * 설문시나리오설정
     */
    @PostMapping("{researchId}/scenario")
    public void scenarioRegister(@Valid @NotNull @RequestBody ResearchTreeFormRequest form, BindingResult bindingResult, @PathVariable Integer researchId) throws IOException, ResultFailException {
        apiInterface.scenarioRegister(researchId, form);
    }
}
