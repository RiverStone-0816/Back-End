package kr.co.eicn.ippbx.front.controller.api.application.sms;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.application.sms.MessageSendService;
import kr.co.eicn.ippbx.model.form.SendMessageFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/send-sms", produces = MediaType.APPLICATION_JSON_VALUE)
public class SmsSendApiController extends BaseController {
    private final MessageSendService sendSmsApiInterface;

    @PostMapping("")
    public void sendSms(@Valid @RequestBody SendMessageFormRequest request, BindingResult bindingResult) throws IOException, ResultFailException {
        sendSmsApiInterface.post(request);
    }
}
