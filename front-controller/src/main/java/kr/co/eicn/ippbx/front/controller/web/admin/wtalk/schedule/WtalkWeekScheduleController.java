package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.schedule;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.schedule.WtalkWeekScheduleApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.enums.DayOfWeek;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/wtalk/schedule/week")
public class WtalkWeekScheduleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkWeekScheduleController.class);

    private final WtalkWeekScheduleApiInterface apiInterface;
    private final OrganizationService organizationService;
    private final WebchatConfigApiInterface webchatConfigApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        final List<WtalkServiceInfoResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().collect(Collectors.toMap(SummaryWtalkScheduleInfoResponse::getParent, SummaryWtalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", scheduleInfos);

        final Map<String, String> dayOfWeeks = FormUtils.optionsOfCode(DayOfWeek.class);
        model.addAttribute("dayOfWeeks", dayOfWeeks);

        return "admin/wtalk/schedule/week/ground";
    }

    @GetMapping("new/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") TalkScheduleInfoFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final Map<String, String> chatBotServices = webchatConfigApiInterface.list().stream().filter(e -> StringUtils.isNotEmpty(e.getSenderKey()))
                .collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        model.addAttribute("chatBotServices", chatBotServices);

        final List<SummaryWtalkScheduleInfoResponse> scheduleInfos = apiInterface.scheduleInfos();
        model.addAttribute("scheduleInfos", scheduleInfos);

        return "admin/wtalk/schedule/week/modal-schedule-info";
    }

    @GetMapping("{seq}/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") TalkScheduleInfoFormUpdateRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final WtalkScheduleInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().filter(e -> entity.getChannelType().equals(e.getChannelType())).collect(Collectors.toMap(SummaryWtalkScheduleInfoResponse::getParent, SummaryWtalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", scheduleInfos);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(form.getGroupCode()));

        return "admin/wtalk/schedule/week/modal-update-schedule-info";
    }

    @GetMapping("modal-view-schedule-group")
    public String modal(Model model, @RequestParam Integer parent) throws IOException, ResultFailException {
        final WtalkScheduleGroupEntity entity = apiInterface.getType(parent);
        model.addAttribute("entity", entity);

        return "admin/wtalk/schedule/week/modal-view-schedule-group";
    }
}