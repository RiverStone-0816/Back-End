package kr.co.eicn.ippbx.front.controller.api.monitor.display;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.model.ScreenDataForByHunt;
import kr.co.eicn.ippbx.front.model.ScreenDataForByService;
import kr.co.eicn.ippbx.front.model.ScreenDataForIntegration;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/screen-data", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScreenDataApiController {
    private static final Logger logger = LoggerFactory.getLogger(ScreenDataApiController.class);

    @Autowired
    private ScreenDataApiInterface screenDataApiInterface;

    @GetMapping("integration")
    public ScreenDataForIntegration integration() throws IOException, ResultFailException {
        return screenDataApiInterface.integration();
    }

    @GetMapping("by-hunt")
    public ScreenDataForByHunt byHunt() throws IOException, ResultFailException {
        return screenDataApiInterface.byHunt();
    }

    @GetMapping("by-service")
    public ScreenDataForByService byService() throws IOException, ResultFailException {
        return screenDataApiInterface.byService();
    }
}
