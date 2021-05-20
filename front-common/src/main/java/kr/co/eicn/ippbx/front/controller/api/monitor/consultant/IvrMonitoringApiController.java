package kr.co.eicn.ippbx.front.controller.api.monitor.consultant;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.IvrMonitoringApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorIvrResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/ivr-monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
public class IvrMonitoringApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IvrMonitoringApiController.class);

    @Autowired
    private IvrMonitoringApiInterface apiInterface;

    @GetMapping("")
    public MonitorIvrResponse list(@RequestParam String serviceNumber) throws IOException, ResultFailException {
        return apiInterface.getIvrList(serviceNumber);
    }
}
