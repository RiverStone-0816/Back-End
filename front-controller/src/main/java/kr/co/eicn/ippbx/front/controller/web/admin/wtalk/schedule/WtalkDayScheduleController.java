package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.schedule;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.HolidayApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.schedule.WtalkDayScheduleApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleGroupEntity;
import kr.co.eicn.ippbx.model.form.DayTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.HolyTalkScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkScheduleInfoFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@AllArgsConstructor
@Controller
@RequestMapping("admin/wtalk/schedule/day")
public class WtalkDayScheduleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkDayScheduleController.class);

    private WtalkDayScheduleApiInterface apiInterface;
    private OrganizationService organizationService;
    private HolidayApiInterface holidayApiInterface;
    private final WebchatConfigApiInterface webchatConfigApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkServiceInfoSearchRequest search) throws IOException, ResultFailException {
        final List<WtalkServiceInfoResponse> list = apiInterface.list(search);
        for (WtalkServiceInfoResponse e : list)
            e.getScheduleInfos().sort(Comparator.comparingLong(e2 -> e2.getFromdate().getTime()));

        model.addAttribute("list", list);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        final Map<String, String> chatBotServices = webchatConfigApiInterface.list().stream().filter(e -> StringUtils.isNotEmpty(e.getSenderKey()))
                .collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        talkServices.putAll(chatBotServices);
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().collect(Collectors.toMap(SummaryWtalkScheduleInfoResponse::getParent, SummaryWtalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", new MapToLinkedHashMap().toLinkedHashMapByValue(scheduleInfos));

        return "admin/wtalk/schedule/day/ground";
    }

    @GetMapping("new/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") DayTalkScheduleInfoFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));

        final Map<String, String> chatBotServices = webchatConfigApiInterface.list().stream().filter(e -> StringUtils.isNotEmpty(e.getSenderKey()))
                .collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        model.addAttribute("chatBotServices", new MapToLinkedHashMap().toLinkedHashMapByValue(chatBotServices));

        final List<SummaryWtalkScheduleInfoResponse> scheduleInfos = apiInterface.scheduleInfos();
        model.addAttribute("scheduleInfos", scheduleInfos);

        return "admin/wtalk/schedule/day/modal-schedule-info";
    }

    @GetMapping("{seq}/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") TalkScheduleInfoFormUpdateRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final WtalkScheduleInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<Integer, String> scheduleInfos = apiInterface.scheduleInfos().stream().filter(e -> entity.getChannelType().equals(e.getChannelType())).collect(Collectors.toMap(SummaryWtalkScheduleInfoResponse::getParent, SummaryWtalkScheduleInfoResponse::getName));
        model.addAttribute("scheduleInfos", new MapToLinkedHashMap().toLinkedHashMapByValue(scheduleInfos));

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(form.getGroupCode()));

        return "admin/wtalk/schedule/day/modal-update-schedule-info";
    }

    @GetMapping("modal-holiday-batch")
    public String modal(Model model, @ModelAttribute("form") HolyTalkScheduleInfoFormRequest form) throws IOException, ResultFailException {
        final List<HolyInfoResponse> holidays = holidayApiInterface.list();
        model.addAttribute("holidays", holidays);

        final List<SummaryWtalkScheduleInfoResponse> scheduleInfos = apiInterface.scheduleInfos();
        model.addAttribute("scheduleInfos", scheduleInfos);

        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));

        final Map<String, String> chatBotServices = webchatConfigApiInterface.list().stream().filter(e -> StringUtils.isNotEmpty(e.getSenderKey()))
                .collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        model.addAttribute("chatBotServices", new MapToLinkedHashMap().toLinkedHashMapByValue(chatBotServices));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        model.addAttribute("thisYear", calendar.get(Calendar.YEAR));

        return "admin/wtalk/schedule/day/modal-holiday-batch";
    }

    @GetMapping("modal-view-schedule-group")
    public String modal(Model model, @RequestParam Integer parent) throws IOException, ResultFailException {
        final WtalkScheduleGroupEntity entity = apiInterface.getType(parent);
        model.addAttribute("entity", entity);

        return "admin/wtalk/schedule/day/modal-view-schedule-group";
    }
}
