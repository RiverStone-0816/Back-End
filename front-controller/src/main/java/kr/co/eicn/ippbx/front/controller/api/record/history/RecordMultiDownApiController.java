package kr.co.eicn.ippbx.front.controller.api.record.history;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordMultiDownApiInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/record-multi-down", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordMultiDownApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordMultiDownApiController.class);

    @Autowired
    private RecordMultiDownApiInterface apiInterface;

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
