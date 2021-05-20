package kr.co.eicn.ippbx.front.controller.api.outbound.research;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchItemApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.form.ResearchItemFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
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
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/research-item", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResearchItemApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ResearchItemApiController.class);

    private final ResearchItemApiInterface apiInterface;

    @GetMapping("")
    public Pagination<ResearchItemResponse> pagination(ResearchItemSearchRequest search) throws IOException, ResultFailException {
        search.setMappingNumber((byte) 0);
        return apiInterface.pagination(search);
    }

    @GetMapping("{seq}")
    public ResearchItemResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void register(@Valid @RequestBody ResearchItemFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.register(form);
    }

    @PutMapping("{seq}")
    public void update(@Valid @RequestBody ResearchItemFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.update(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     * 음원 목록 조회
     */
    @GetMapping("add-sounds")
    public List<SummarySoundListResponse> addSounds() throws IOException, ResultFailException {
        return apiInterface.addSounds();
    }

    /**
     * 설문문항 조회
     */
    @GetMapping("add-research-item")
    public List<SummaryResearchItemResponse> addResearchItem() throws IOException, ResultFailException {
        return apiInterface.addResearchItem();
    }
}
