package kr.co.eicn.ippbx.front.controller.web.admin.chatbot.event;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.chatbot.event.ChatbotEventHistoryApiInterface;
import kr.co.eicn.ippbx.model.search.ChatbotEventHistorySearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@AllArgsConstructor
@Controller
@RequestMapping("admin/chatbot/event/history")
public class ChatbotEventHistoryController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotEventHistoryController.class);

    private final ChatbotEventHistoryApiInterface chatbotEventHistoryApiInterface;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String getPagination(Model model, @ModelAttribute("search") ChatbotEventHistorySearchRequest search) throws IOException, ResultFailException {
        model.addAttribute("pagination", chatbotEventHistoryApiInterface.getPagination(search));
        model.addAttribute("chatbotEventList", searchApiInterface.getChatbotEventList(""));

        return "admin/chatbot/event/history/ground";
    }
}
