package kr.co.eicn.ippbx.front.controller.api.sounds.sounds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.ArsForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.ArsApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SoundDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SoundListSummaryResponse;
import kr.co.eicn.ippbx.model.search.SoundListSearchRequest;
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
@RequestMapping(value = "api/ars", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArsApiController extends BaseController {
    protected static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";
    private static final Logger logger = LoggerFactory.getLogger(ArsApiController.class);
    @Autowired
    private ArsApiInterface apiInterface;

    @GetMapping("")
    public Pagination<SoundListSummaryResponse> pagination(SoundListSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{seq}")
    public SoundDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody ArsForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @PutMapping("{seq}/web-log")
    public void updateWebLog(@PathVariable Integer seq, @RequestParam String type) throws IOException, ResultFailException {
        apiInterface.updateWebLog(seq, type);
    }
}
