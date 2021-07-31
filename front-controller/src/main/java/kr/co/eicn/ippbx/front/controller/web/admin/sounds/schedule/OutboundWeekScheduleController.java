package kr.co.eicn.ippbx.front.controller.web.admin.sounds.schedule;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.OutboundWeekScheduleApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.model.dto.eicn.OutScheduleSeedDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OutScheduleSeedEntity;
import kr.co.eicn.ippbx.model.enums.DayOfWeek;
import kr.co.eicn.ippbx.model.form.OutScheduleSeedFormRequest;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/sounds/schedule/outbound-week")
public class OutboundWeekScheduleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundWeekScheduleController.class);

    @Autowired
    private OutboundWeekScheduleApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") OutScheduleSeedSearchRequest search) throws IOException, ResultFailException {
        final List<OutScheduleSeedEntity> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final Map<String, String> dayOfWeeks = FormUtils.optionsOfCode(DayOfWeek.class);
        model.addAttribute("dayOfWeeks", dayOfWeeks);

        return "admin/sounds/schedule/outbound-week/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") OutScheduleSeedFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> extensions = apiInterface.addExtensions().stream().collect(Collectors.toMap(SummaryPhoneInfoResponse::getExtension, e -> defaultString(e.getInUseIdName())));
        model.addAttribute("extensions", extensions);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        final Map<String, String> dayOfWeeks = FormUtils.optionsOfCode(DayOfWeek.class);
        model.addAttribute("dayOfWeeks", dayOfWeeks);

        return "admin/sounds/schedule/outbound-week/modal";
    }

    @GetMapping("{parent}/modal")
    public String modal(Model model, @ModelAttribute("form") OutScheduleSeedFormRequest form, @PathVariable Integer parent) throws IOException, ResultFailException {
        final OutScheduleSeedDetailResponse entity = apiInterface.get(parent);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        form.setWeeks(new HashSet<>(entity.getWeeks()));

        final Map<String, String> extensions = apiInterface.addExtensions().stream()
                .filter(e -> e.getExtension() != null && e.getInUseIdName() != null)
                .collect(Collectors.toMap(SummaryPhoneInfoResponse::getExtension, SummaryPhoneInfoResponse::getInUseIdName));
        model.addAttribute("extensions", extensions);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        final Map<String, String> dayOfWeeks = FormUtils.optionsOfCode(DayOfWeek.class);
        model.addAttribute("dayOfWeeks", dayOfWeeks);

        for (SummaryPhoneInfoResponse e : entity.getExtensions())
            extensions.remove(e.getExtension());

        return "admin/sounds/schedule/outbound-week/modal";
    }
}
