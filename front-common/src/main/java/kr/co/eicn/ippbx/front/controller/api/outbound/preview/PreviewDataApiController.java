package kr.co.eicn.ippbx.front.controller.api.outbound.preview;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewDataApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoRedistributionFormRequest;
import kr.co.eicn.ippbx.model.search.PrvCustomInfoSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/preview-data", produces = MediaType.APPLICATION_JSON_VALUE)
public class PreviewDataApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PreviewDataApiController.class);

    private final PreviewDataApiInterface apiInterface;

    @GetMapping("preview-group")
    public List<PrvGroup> prvGroup() throws IOException, ResultFailException {
        return apiInterface.prvGroup();
    }

    @GetMapping("{groupSeq}/data")
    public Pagination<PrvCustomInfoEntity> getPagination(@PathVariable Integer groupSeq, PrvCustomInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getPagination(groupSeq, search);
    }

    @PostMapping("{groupSeq}/data/redistribution")
    public void redistribution(@PathVariable Integer groupSeq, @Valid @RequestBody PrvCustomInfoRedistributionFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.redistribution(groupSeq, form);
    }

    @GetMapping("{groupSeq}/data/{customId}")
    public PrvCustomInfoEntity get(@PathVariable Integer groupSeq, @PathVariable String customId) throws IOException, ResultFailException {
        return apiInterface.get(groupSeq, customId);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody PrvCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{customId}")
    public void put(@Valid @RequestBody PrvCustomInfoFormRequest form, BindingResult bindingResult, @PathVariable String customId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        apiInterface.put(customId, form);
    }

    @DeleteMapping("{groupSeq}/data/{customId}")
    public void delete(@PathVariable Integer groupSeq, @PathVariable String customId) throws IOException, ResultFailException {
        apiInterface.delete(groupSeq, customId);
    }
}
