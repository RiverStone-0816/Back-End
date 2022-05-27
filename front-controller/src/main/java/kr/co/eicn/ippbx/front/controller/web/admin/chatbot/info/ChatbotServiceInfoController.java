package kr.co.eicn.ippbx.front.controller.web.admin.chatbot.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.wtalk.info.WtalkServiceApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceSummaryResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("admin/chatbot/info/service")
public class ChatbotServiceInfoController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotServiceInfoController.class);

    private final WtalkServiceApiInterface talkServiceApiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<WtalkServiceSummaryResponse> list = talkServiceApiInterface.list();
        model.addAttribute("list", list);

        return "admin/chatbot/info/service/ground";
    }
}
