package kr.co.eicn.ippbx.front.controller.api.service;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.ContextApiInterface;
import kr.co.eicn.ippbx.server.model.form.ContextInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/context", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContextApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ContextApiController.class);

    @Autowired
    private ContextApiInterface apiInterface;

    /**
     * 컨텍스트추가
     **/
    @PostMapping("")
    public void post(@Valid @RequestBody ContextInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 컨텍스트수정
     **/
    @PutMapping(value = "{context}")
    public void put(@Valid @RequestBody ContextInfoFormRequest form, BindingResult bindingResult, @PathVariable String context) throws IOException, ResultFailException {
        apiInterface.put(context, form);
    }

    /**
     * 컨텍스트삭제
     **/
    @DeleteMapping(value = "{context}")
    public void delete(@PathVariable String context) throws IOException, ResultFailException {
        apiInterface.delete(context);
    }

    /**
     * 보이는 ARS 적용
     **/
    @PostMapping(value = "{context}/apply")
    public void apply(@Valid @RequestBody WebVoiceItemsFormRequest form, BindingResult bindingResult, @PathVariable String context) throws IOException, ResultFailException {
        apiInterface.apply(context, form);
    }
}
