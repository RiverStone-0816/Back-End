package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsHistoryApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.PDSHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/pds-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsHistoryApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsHistoryApiController.class);

    @Autowired
    private PdsHistoryApiInterface apiInterface;

    @GetMapping("")
    public Pagination<PDSHistoryResponse> pagination(PDSHistorySearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @DeleteMapping("execution/{executeId}")
    public void delete(@PathVariable String executeId) throws IOException, ResultFailException {
        apiInterface.delete(executeId);
    }
}
