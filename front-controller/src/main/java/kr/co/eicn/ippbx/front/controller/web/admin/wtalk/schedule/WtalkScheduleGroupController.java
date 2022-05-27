package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.schedule;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.ChatbotApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.schedule.WtalkScheduleGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.form.TalkScheduleGroupListFormRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/wtalk/schedule/schedule-group")
public class WtalkScheduleGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkScheduleGroupController.class);

    @Autowired
    private WtalkScheduleGroupApiInterface apiInterface;
    @Autowired
    private ChatbotApiInterface chatbotApiInterface;
    @Autowired
    private WtalkReceptionGroupApiInterface wtalkReceptionGroupApiInterface;


    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<WtalkScheduleGroupSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/wtalk/schedule/schedule-group/ground";
    }

    @GetMapping("{parent}/item/new/modal")
    public String modalScheduleItem(Model model, @ModelAttribute("form") TalkScheduleGroupListFormRequest form, @PathVariable Integer parent, @RequestParam String channelType) throws IOException, ResultFailException {
        form.setParent(parent);

        model.addAttribute("isChatbot", TalkChannelType.EICN.getCode().equals(channelType));
        final Map<String, String> talkMentsOfStringSeq = apiInterface.getTalkMent().stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryWtalkMentResponse::getMentName));
        model.addAttribute("talkMentsOfStringSeq", talkMentsOfStringSeq);
        final Map<String, String> chatbotList = chatbotApiInterface.list().stream().collect(Collectors.toMap(e-> e.getId().toString(), WebchatBotSummaryInfoResponse::getName));
        model.addAttribute("chatbotList", chatbotList);
        final Map<String, String> talkGroupList = wtalkReceptionGroupApiInterface.list().stream().collect(Collectors.toMap(e -> e.getGroupId().toString(), WtalkMemberGroupSummaryResponse::getGroupName));
        model.addAttribute("talkGroupList", talkGroupList);

        return "admin/wtalk/schedule/schedule-group/modal-schedule-item";
    }

    @GetMapping("{parent}/item/{child}/modal")
    public String modalScheduleItem(Model model, @ModelAttribute("form") TalkScheduleGroupListFormRequest form, @PathVariable Integer parent, @PathVariable Integer child, @RequestParam String channelType) throws IOException, ResultFailException {
        final WtalkScheduleGroupListDetailResponse entity = apiInterface.getItem(child);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return modalScheduleItem(model, form, parent, channelType);
    }
}
