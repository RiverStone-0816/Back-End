package kr.co.eicn.ippbx.front.controller.api.service.log;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.log.WebLogApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.LoginInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/web-log", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebLogApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebLogApiController.class);

    @Autowired
    private WebLogApiInterface apiInterface;

    @GetMapping("{userId}/last-login")
    public LoginInfoResponse getLastLoginInfo(@PathVariable String userId) throws IOException, ResultFailException {
        return apiInterface.getLastLoginInfo(userId);
    }

    @DeleteMapping("")
    public void delete(@RequestParam List<Integer> checkIds) throws IOException, ResultFailException {
        apiInterface.delete(checkIds);
    }

    @DeleteMapping("overwrite/{limit}")
    public void overwrite(@PathVariable Integer limit) throws IOException, ResultFailException {
        apiInterface.overwrite(limit);
    }

    @PutMapping("down/{type}")
    public void updateWebLogDown(@PathVariable String type) throws IOException, ResultFailException {
        apiInterface.updateWebLogDown(type);
    }
}
