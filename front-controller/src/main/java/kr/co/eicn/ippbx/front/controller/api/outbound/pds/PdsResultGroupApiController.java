package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResultGroupApiInterface;
import kr.co.eicn.ippbx.model.form.PDSResultGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PDSResultGroupUpdateFormRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@Slf4j
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/pds-result-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsResultGroupApiController extends BaseController {
    private final PdsResultGroupApiInterface apiInterface;

    @PostMapping("")
    public String post(@Valid @RequestBody PDSResultGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{name}")
    public void update(@Valid @RequestBody PDSResultGroupUpdateFormRequest form, BindingResult bindingResult, @PathVariable String name) throws IOException, ResultFailException {
        apiInterface.update(name, form);
    }

    @DeleteMapping("{name}")
    public void delete(@PathVariable String name) throws IOException, ResultFailException {
        apiInterface.delete(name);
    }
}
