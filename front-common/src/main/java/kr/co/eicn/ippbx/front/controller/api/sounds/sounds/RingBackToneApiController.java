package kr.co.eicn.ippbx.front.controller.api.sounds.sounds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.RingBackToneForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.RingBackToneApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.MohDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MohListSummaryResponse;
import kr.co.eicn.ippbx.model.search.MohListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/ring-back-tone", produces = MediaType.APPLICATION_JSON_VALUE)
public class RingBackToneApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RingBackToneApiController.class);

    @Autowired
    private RingBackToneApiInterface apiInterface;

    @GetMapping("")
    public Pagination<MohListSummaryResponse> pagination(MohListSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{category}")
    public MohDetailResponse get(@PathVariable String category) throws IOException, ResultFailException {
        return apiInterface.get(category);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody RingBackToneForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @DeleteMapping("{category}")
    public void delete(@PathVariable String category) throws IOException, ResultFailException {
        apiInterface.delete(category);
    }

    @PutMapping("{category}/web-log")
    public void updateWebLog(@PathVariable String category, @RequestParam String type) throws IOException, ResultFailException {
        apiInterface.updateWebLog(category, type);
    }
}
