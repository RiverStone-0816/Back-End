package kr.co.eicn.ippbx.front.controller.api.outbound.preview;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewResultApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PrvResultCustomInfoSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/preview-result", produces = MediaType.APPLICATION_JSON_VALUE)
public class PreviewResultApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PreviewResultApiController.class);

    private final PreviewResultApiInterface apiInterface;

    @GetMapping("{groupSeq}/data")
    public Pagination<PrvResultCustomInfoEntity> getPagination(@PathVariable Integer groupSeq, PrvResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getPagination(groupSeq, search);
    }

    @GetMapping("{groupSeq}/data/{seq}")
    public PrvResultCustomInfoEntity get(@PathVariable Integer groupSeq, @PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(groupSeq, seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        form.setFromOrg("PRV");
        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{groupSeq}/data/{seq}")
    public void delete(@PathVariable Integer groupSeq, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(groupSeq, seq);
    }
}
