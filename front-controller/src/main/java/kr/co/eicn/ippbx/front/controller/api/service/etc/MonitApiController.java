package kr.co.eicn.ippbx.front.controller.api.service.etc;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.etc.MonitApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.model.form.MonitControlChangeRequest;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
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
@LoginRequired
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/monit", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MonitApiController.class);

    @Autowired
    private MonitApiInterface apiInterface;

    @GetMapping("")
    public List<MonitControlResponse> list(MonitControlSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.listDashboard(search);
    }

    @PutMapping("")
    public void put(@Valid @RequestBody MonitControlChangeRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(form);
    }
}
