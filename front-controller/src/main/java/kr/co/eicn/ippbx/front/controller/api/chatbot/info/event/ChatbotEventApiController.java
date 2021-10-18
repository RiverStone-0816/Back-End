package kr.co.eicn.ippbx.front.controller.api.chatbot.info.event;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.chatbot.event.ChatbotEventHistoryApiInterface;
import kr.co.eicn.ippbx.model.form.ChatbotSendEventFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@LoginRequired
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/chatbot/event/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotEventApiController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotEventApiController.class);

    private final ChatbotEventHistoryApiInterface chatbotEventHistoryApiInterface;

    @PostMapping("")
    public void post(@RequestBody ChatbotSendEventFormRequest form) throws IOException, ResultFailException {
        chatbotEventHistoryApiInterface.post(form);
    }
}

