package kr.co.eicn.ippbx.front.controller.web.admin.sounds.schedule;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.HolidayApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.OutboundDayScheduleApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.HolyInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OutScheduleSeedDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.model.form.DayOutScheduleSeedFormRequest;
import kr.co.eicn.ippbx.model.form.HolyOutScheduleFormRequest;
import kr.co.eicn.ippbx.model.search.OutScheduleSeedSearchRequest;
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
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/sounds/schedule/outbound-day")
public class OutboundDayScheduleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundDayScheduleController.class);

    @Autowired
    private OutboundDayScheduleApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private HolidayApiInterface holidayApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") OutScheduleSeedSearchRequest search) throws IOException, ResultFailException {
        final List<OutScheduleSeedEntity> list = apiInterface.list(search);
        model.addAttribute("list", list);

        return "admin/sounds/schedule/outbound-day/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") DayOutScheduleSeedFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> extensions = apiInterface.addExtensions().stream().collect(Collectors.toMap(SummaryPhoneInfoResponse::getExtension, e -> defaultString(e.getInUseIdName())));
        model.addAttribute("extensions", extensions);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        return "admin/sounds/schedule/outbound-day/modal";
    }

    @GetMapping("{parent}/modal")
    public String modal(Model model, @ModelAttribute("form") DayOutScheduleSeedFormRequest form, @PathVariable Integer parent) throws IOException, ResultFailException {
        final OutScheduleSeedDetailResponse entity = apiInterface.get(parent);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        if (entity.getFromtime() != null)
            form.setFromDate(g.dateFormat(entity.getFromtime()));
        if (entity.getTotime() != null)
            form.setToDate(g.dateFormat(entity.getTotime()));

        final Map<String, String> extensions = apiInterface.addExtensions().stream()
                .filter(e -> e.getExtension() != null && e.getInUseIdName() != null)
                .collect(Collectors.toMap(SummaryPhoneInfoResponse::getExtension, SummaryPhoneInfoResponse::getInUseIdName));
        model.addAttribute("extensions", extensions);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        for (SummaryPhoneInfoResponse extension : entity.getExtensions())
            extensions.remove(extension.getExtension());

        return "admin/sounds/schedule/outbound-day/modal";
    }

    @GetMapping("modal-holiday-batch")
    public String modalHolidayBatch(Model model, @ModelAttribute("form") HolyOutScheduleFormRequest form) throws IOException, ResultFailException {
        final List<HolyInfoResponse> holidays = holidayApiInterface.list();
        model.addAttribute("holidays", holidays);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        model.addAttribute("thisYear", calendar.get(Calendar.YEAR));

        return "admin/sounds/schedule/outbound-day/modal-holiday-batch";
    }
}
