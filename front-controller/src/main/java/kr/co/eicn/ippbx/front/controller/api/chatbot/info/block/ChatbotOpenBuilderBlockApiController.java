package kr.co.eicn.ippbx.front.controller.api.chatbot.info.block;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.chatbot.info.ChatbotOpenBuilderBlockApiInterface;
import kr.co.eicn.ippbx.model.form.ChatbotOpenBuilderBlockFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@LoginRequired
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/chatbot/info/block", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotOpenBuilderBlockApiController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotOpenBuilderBlockApiController.class);

    private final ChatbotOpenBuilderBlockApiInterface chatbotOpenBuilderBlockApiInterface;

    @PostMapping("")
    public void post(@RequestBody ChatbotOpenBuilderBlockFormRequest form) throws IOException, ResultFailException {
        chatbotOpenBuilderBlockApiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void post(@PathVariable Integer seq, @RequestBody ChatbotOpenBuilderBlockFormRequest form) throws IOException, ResultFailException {
        chatbotOpenBuilderBlockApiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        chatbotOpenBuilderBlockApiInterface.delete(seq);
    }
}

