package kr.co.eicn.ippbx.front.controller.web.admin.chatbot.event;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.chatbot.event.ChatbotKakaoCustomerProfileApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkServiceInfo;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotSendEventDataResponse;
import kr.co.eicn.ippbx.model.form.ChatbotSendEventFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotKakaoCustomerProfileSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("admin/chatbot/event/profile")
public class ChatbotKakaoCustomerProfileController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotKakaoCustomerProfileController.class);

    private final ChatbotKakaoCustomerProfileApiInterface chatbotKakaoCustomerProfileApiInterface;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String getPagination(Model model, @ModelAttribute("search") ChatbotKakaoCustomerProfileSearchRequest search) throws IOException, ResultFailException {
        model.addAttribute("pagination", chatbotKakaoCustomerProfileApiInterface.getPagination(search));
        model.addAttribute("talkServiceList", searchApiInterface.getChatbotServiceInfoList().stream().collect(Collectors.toMap(TalkServiceInfo::getBotId, TalkServiceInfo::getBotName)));

        return "admin/chatbot/event/profile/ground";
    }

    @GetMapping("send/event/{seq}")
    public String sendEvent(Model model, @ModelAttribute("form") ChatbotSendEventFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final ChatbotSendEventDataResponse entity = chatbotKakaoCustomerProfileApiInterface.get(seq);
        model.addAttribute("entity", entity);
        model.addAttribute("eventList", searchApiInterface.getChatbotEventList(entity.getBotId()));

        form.setBotId(entity.getBotId());
        form.setBotName(entity.getBotName());
        if (StringUtils.isNotEmpty(entity.getRequestUserId())) {
            form.setUserId(entity.getRequestUserId());
            form.setUserType("botUserKey");
        } else if (StringUtils.isNotEmpty(entity.getRequestUserPlusFriendUserKey())) {
            form.setUserId(entity.getRequestUserPlusFriendUserKey());
            form.setUserType("plusfriendUserKey");
        } else if (StringUtils.isNotEmpty(entity.getAppUserId())) {
            form.setUserId(entity.getAppUserId());
            form.setUserType("appUserId");
        }
        form.setUserName(entity.getNickName());

        return "admin/chatbot/event/profile/modal-send-event";
    }
}
