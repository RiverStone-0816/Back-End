package kr.co.eicn.ippbx.front.controller.api.application.stt;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.application.stt.SttTextApiInterface;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/stt-text", produces = MediaType.APPLICATION_JSON_VALUE)
public class SttTextController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SttTextController.class);

    @Autowired
    private SttTextApiInterface apiInterface;

    @PutMapping("remind/{messageId}")
    public void updateRemind(@PathVariable String messageId) throws IOException, ResultFailException {
        apiInterface.updateRemind(messageId);
    }
}
