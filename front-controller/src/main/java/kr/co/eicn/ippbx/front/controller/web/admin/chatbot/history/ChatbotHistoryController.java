package kr.co.eicn.ippbx.front.controller.web.admin.chatbot.history;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.chatbot.history.ChatbotHistoryApiInterface;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotHistoryResponse;
import kr.co.eicn.ippbx.model.search.ChatbotHistorySearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
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
@RequestMapping("admin/chatbot/history")
public class ChatbotHistoryController  extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ChatbotHistoryController.class);

    private final ChatbotHistoryApiInterface apiInterface;

    @GetMapping("")
    public String pagination(Model model, @ModelAttribute("search") ChatbotHistorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<ChatbotHistoryResponse> pagination = apiInterface.getPagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/chatbot/history/ground";
    }
}
