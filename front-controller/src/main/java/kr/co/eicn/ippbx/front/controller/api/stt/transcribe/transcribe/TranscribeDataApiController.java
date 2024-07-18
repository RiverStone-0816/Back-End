package kr.co.eicn.ippbx.front.controller.api.stt.transcribe.transcribe;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.api.wtalk.group.WtalkReceptionGroupApiController;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeDataApiInterface;
import kr.co.eicn.ippbx.model.form.TranscribeDataFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/transcribe-data", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscribeDataApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkReceptionGroupApiController.class);

    @Autowired
    private TranscribeDataApiInterface apiInterface;

    @PostMapping
    public Integer post(@Valid @RequestBody TranscribeDataFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}/{status}")
    public void post(@PathVariable Integer seq, @PathVariable String status) throws IOException, ResultFailException {
        apiInterface.updateLearnStatus(seq, status);
    }
}
