package kr.co.eicn.ippbx.front.controller.api.stt.transcribe.learn;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.api.wtalk.group.WtalkReceptionGroupApiController;
import kr.co.eicn.ippbx.front.service.api.stt.learn.LearnGroupApiInterface;
import kr.co.eicn.ippbx.model.form.LearnGroupFormRequest;
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
@RequestMapping(value = "api/learn-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class LearnGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkReceptionGroupApiController.class);

    @Autowired
    private LearnGroupApiInterface apiInterface;

    @PostMapping
    public Integer post(@Valid @RequestBody LearnGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PostMapping("{seq}/execute")
    public void put(@Valid @RequestBody LearnGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @PutMapping("status/{seq}")
    public void statusUpdate(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.statusUpdate(seq);
    }


}
