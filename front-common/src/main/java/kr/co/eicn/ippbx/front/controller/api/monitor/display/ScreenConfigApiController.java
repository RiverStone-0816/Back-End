package kr.co.eicn.ippbx.front.controller.api.monitor.display;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenConfigApiInterface;
import kr.co.eicn.ippbx.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.model.form.ScreenConfigFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping(value = "api/screen-config", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScreenConfigApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ScreenConfigApiController.class);

    @Autowired
    private ScreenConfigApiInterface apiInterface;

    @GetMapping("")
    public List<ScreenConfigEntity> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping(value = "{seq}")
    public ScreenConfigEntity get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody ScreenConfigFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping(value = "{seq}")
    public void put(@Valid @RequestBody ScreenConfigFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping(value = "{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
