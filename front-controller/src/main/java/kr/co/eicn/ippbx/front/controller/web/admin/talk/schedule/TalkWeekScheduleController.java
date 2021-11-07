package kr.co.eicn.ippbx.front.controller.web.admin.talk.schedule;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.schedule.TalkWeekScheduleApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkScheduleInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkScheduleInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceInfoResponse;
import kr.co.eicn.ippbx.model.entity.eicn.TalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.enums.DayOfWeek;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
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
@RequestMapping("admin/talk/schedule/week")
public class TalkWeekScheduleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkWeekScheduleController.class);

    @Autowired
    private TalkWeekScheduleApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        final List<TalkServiceInfoResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryTalkServiceResponse::getSenderKey, SummaryTalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().collect(Collectors.toMap(SummaryTalkScheduleInfoResponse::getParent, SummaryTalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", scheduleInfos);

        final Map<String, String> dayOfWeeks = FormUtils.optionsOfCode(DayOfWeek.class);
        model.addAttribute("dayOfWeeks", dayOfWeeks);

        return "admin/talk/schedule/week/ground";
    }

    @GetMapping("new/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") TalkScheduleInfoFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryTalkServiceResponse::getSenderKey, SummaryTalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().collect(Collectors.toMap(SummaryTalkScheduleInfoResponse::getParent, SummaryTalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", scheduleInfos);

        return "admin/talk/schedule/week/modal-schedule-info";
    }

    @GetMapping("{seq}/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") TalkScheduleInfoFormUpdateRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final TalkScheduleInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().collect(Collectors.toMap(SummaryTalkScheduleInfoResponse::getParent, SummaryTalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", scheduleInfos);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(form.getGroupCode()));

        return "admin/talk/schedule/week/modal-update-schedule-info";
    }

    @GetMapping("modal-view-schedule-group")
    public String modal(Model model, @RequestParam Integer parent) throws IOException, ResultFailException {
        final TalkScheduleGroupEntity entity = apiInterface.getType(parent);
        model.addAttribute("entity", entity);

        return "admin/talk/schedule/week/modal-view-schedule-group";
    }
}
