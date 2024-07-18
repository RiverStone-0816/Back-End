package kr.co.eicn.ippbx.front.controller.api.stt.transcribe.transcribe;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.api.wtalk.group.WtalkReceptionGroupApiController;
import kr.co.eicn.ippbx.front.model.form.TranscribeFileForm;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeGroupApiInterface;
import kr.co.eicn.ippbx.model.form.TranscribeGroupFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(value = "api/transcribe-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscribeGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkReceptionGroupApiController.class);

    @Autowired
    private TranscribeGroupApiInterface apiInterface;

    @PostMapping
    public Integer post(@Valid @RequestBody TranscribeGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody TranscribeGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @PostMapping("upload/{seq}")
    public void upload(@Valid @RequestBody TranscribeFileForm form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.upload(seq, form);
    }

    @PostMapping("{fileSeq}/execute")
    public Integer executeStt(@PathVariable Integer fileSeq) throws IOException, ResultFailException {
        return apiInterface.executeStt(fileSeq);
    }

    @PutMapping("status/{seq}")
    public void statusUpdate(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.statusUpdate(seq);
    }

}
