package kr.co.eicn.ippbx.front.controller.api.record.file;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.file.RecordRemoteFileApiInterface;
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
@RequestMapping(value = "api/record-remote-file", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordRemoteFileApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordRemoteFileApiController.class);

    @Autowired
    private RecordRemoteFileApiInterface apiInterface;

    @DeleteMapping("{fileName}")
    public void delete(@PathVariable String fileName) throws IOException, ResultFailException {
        apiInterface.delete(fileName);
    }
}
