package kr.co.eicn.ippbx.front.controller.web.admin.sounds.schedule;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.HolidayApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.InboundDayScheduleApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.model.enums.DayOfWeek;
import kr.co.eicn.ippbx.model.form.DayScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.form.DayScheduleInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.HolyScheduleInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ScheduleInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Controller
@RequestMapping("admin/sounds/schedule/inbound-day")
public class InboundDayScheduleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(InboundDayScheduleController.class);

    @Autowired
    private InboundDayScheduleApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private HolidayApiInterface holidayApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") ScheduleInfoSearchRequest search) throws IOException, ResultFailException {
        final List<Number070ScheduleInfoResponse> list = apiInterface.list(search);
        for (Number070ScheduleInfoResponse info : list) {
            info.getScheduleInfos().sort(Comparator.comparingLong(e -> e.getFromdate().getTime()));
        }

        model.addAttribute("list", list);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        final List<SummaryNumber070Response> numbers = apiInterface.searchNumbers();
        model.addAttribute("numbers", numbers);

        final Map<Integer, String> groups = apiInterface.scheduleGroups().stream().collect(Collectors.toMap(SummaryScheduleGroupResponse::getParent, SummaryScheduleGroupResponse::getName));
        model.addAttribute("groups", groups);

        final Map<String, String> dayOfWeeks = FormUtils.optionsOfCode(DayOfWeek.class);
        model.addAttribute("dayOfWeeks", dayOfWeeks);

        return "admin/sounds/schedule/inbound-day/ground";
    }

    @GetMapping("new/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") DayScheduleInfoFormRequest form) throws IOException, ResultFailException {
        final Map<Integer, String> scheduleGroups = apiInterface.scheduleGroups().stream().collect(Collectors.toMap(SummaryScheduleGroupResponse::getParent, SummaryScheduleGroupResponse::getName));
        model.addAttribute("scheduleGroups", scheduleGroups);

        final List<SummaryNumber070Response> number070List = apiInterface.addNumber070List();
        model.addAttribute("number070List", number070List);

        return "admin/sounds/schedule/inbound-day/modal-schedule-info";
    }

    @GetMapping("{seq}/modal-schedule-info")
    public String modal(Model model, @ModelAttribute("form") DayScheduleInfoUpdateFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final Number070ScheduleInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity.getScheduleInfo());
        form.setFromDate(entity.getScheduleInfo().getFromdate().toString());

        final Map<Integer, String> scheduleGroups = apiInterface.scheduleGroups().stream().collect(Collectors.toMap(SummaryScheduleGroupResponse::getParent, SummaryScheduleGroupResponse::getName));
        model.addAttribute("scheduleGroups", scheduleGroups);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(form.getGroupCode()));

        return "admin/sounds/schedule/inbound-day/modal-update-schedule-info";
    }

    @GetMapping("modal-view-schedule-group")
    public String modal(Model model, @RequestParam Integer parent) throws IOException, ResultFailException {
        final ScheduleGroupEntity entity = apiInterface.getType(parent);
        model.addAttribute("entity", entity);

        return "admin/sounds/schedule/inbound-day/modal-view-schedule-group";
    }

    @GetMapping("modal-holiday-batch")
    public String modalHolidayBatch(Model model, @ModelAttribute("form") HolyScheduleInfoFormRequest form) throws IOException, ResultFailException {
        final List<HolyInfoResponse> holidays = holidayApiInterface.list();
        model.addAttribute("holidays", holidays);

        final Map<Integer, String> scheduleGroups = apiInterface.scheduleGroups().stream().collect(Collectors.toMap(SummaryScheduleGroupResponse::getParent, SummaryScheduleGroupResponse::getName));
        model.addAttribute("scheduleGroups", scheduleGroups);

        final Map<String, String> number070List = apiInterface.addNumber070List().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, e -> Optional.ofNullable(e.getHanName()).orElse("")));
        model.addAttribute("number070List", number070List);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        model.addAttribute("thisYear", calendar.get(Calendar.YEAR));

        return "admin/sounds/schedule/inbound-day/modal-holiday-batch";
    }
}
