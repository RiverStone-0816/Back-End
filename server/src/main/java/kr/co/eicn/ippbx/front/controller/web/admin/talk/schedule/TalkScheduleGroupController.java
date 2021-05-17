package kr.co.eicn.ippbx.front.controller.web.admin.talk.schedule;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.schedule.TalkScheduleGroupApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.form.TalkScheduleGroupListFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/talk/schedule/schedule-group")
public class TalkScheduleGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkScheduleGroupController.class);

    @Autowired
    private TalkScheduleGroupApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<TalkScheduleGroupSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/talk/schedule/schedule-group/ground";
    }

    @GetMapping("{parent}/item/new/modal")
    public String modalScheduleItem(Model model, @ModelAttribute("form") TalkScheduleGroupListFormRequest form, @PathVariable Integer parent) throws IOException, ResultFailException {
        form.setParent(parent);

        final List<SummaryTalkMentResponse> talkMentList = apiInterface.getTalkMent();
        final Map<String, String> talkMentsOfStringSeq = talkMentList.stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryTalkMentResponse::getMentName));
        model.addAttribute("talkMentsOfStringSeq", talkMentsOfStringSeq);
        final Map<Integer, String> talkMentsOfIntegerSeq = talkMentList.stream().collect(Collectors.toMap(SummaryTalkMentResponse::getSeq, SummaryTalkMentResponse::getMentName));
        model.addAttribute("talkMentsOfIntegerSeq", talkMentsOfIntegerSeq);

        return "admin/talk/schedule/schedule-group/modal-schedule-item";
    }

    @GetMapping("{parent}/item/{child}/modal")
    public String modalScheduleItem(Model model, @ModelAttribute("form") TalkScheduleGroupListFormRequest form, @PathVariable Integer parent, @PathVariable Integer child) throws IOException, ResultFailException {
        final TalkScheduleGroupListDetailResponse entity = apiInterface.getItem(child);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return modalScheduleItem(model, form, parent);
    }
}
