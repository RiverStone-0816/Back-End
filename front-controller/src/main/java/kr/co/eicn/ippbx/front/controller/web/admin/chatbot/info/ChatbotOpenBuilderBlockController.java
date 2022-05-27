package kr.co.eicn.ippbx.front.controller.web.admin.chatbot.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.chatbot.info.ChatbotOpenBuilderBlockApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotOpenBuilderInfoResponse;
import kr.co.eicn.ippbx.model.enums.ChatbotOpenBuilderType;
import kr.co.eicn.ippbx.model.form.ChatbotOpenBuilderBlockFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotOpenBuilderBlockSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@RequestMapping("admin/chatbot/info/block")
public class ChatbotOpenBuilderBlockController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotOpenBuilderBlockController.class);

    private final ChatbotOpenBuilderBlockApiInterface chatbotOpenBuilderBlockApiInterface;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String pagination(Model model, @ModelAttribute("search") ChatbotOpenBuilderBlockSearchRequest search) throws IOException, ResultFailException {
        model.addAttribute("pagination", chatbotOpenBuilderBlockApiInterface.getPagination(search));
        model.addAttribute("talkServiceList", searchApiInterface.getChatbotServiceInfoList().stream().collect(Collectors.toMap(WtalkServiceInfo::getBotId, WtalkServiceInfo::getBotName)));

        return "admin/chatbot/info/block/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ChatbotOpenBuilderBlockFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("talkServiceList", searchApiInterface.getChatbotServiceInfoList().stream().collect(Collectors.toMap(WtalkServiceInfo::getBotId, WtalkServiceInfo::getBotName)));
        model.addAttribute("openBuilderType", FormUtils.optionsOfCode(ChatbotOpenBuilderType.class));

        return "admin/chatbot/info/block/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ChatbotOpenBuilderBlockFormRequest form ) throws IOException, ResultFailException {
        model.addAttribute("talkServiceList", searchApiInterface.getChatbotServiceInfoList().stream().collect(Collectors.toMap(WtalkServiceInfo::getBotId, WtalkServiceInfo::getBotName)));
        model.addAttribute("openBuilderType", FormUtils.optionsOfCode(ChatbotOpenBuilderType.class));

        final ChatbotOpenBuilderInfoResponse entity = chatbotOpenBuilderBlockApiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return "admin/chatbot/info/block/modal";
    }
}
