package kr.co.eicn.ippbx.front.controller.web.admin.sounds.schedule;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.ScheduleGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.ScheduleGroupListFormRequest;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/sounds/schedule/schedule-group")
public class ScheduleGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleGroupController.class);

    @Autowired
    private ScheduleGroupApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<ScheduleGroupSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/sounds/schedule/schedule-group/ground";
    }

    @GetMapping("{parent}/item/new/modal")
    public String modalScheduleItem(Model model, @ModelAttribute("form") ScheduleGroupListFormRequest form, @PathVariable Integer parent) throws IOException, ResultFailException {
        final List<SummaryContextInfoResponse> contextList = apiInterface.addContextList();
        model.addAttribute("contextList", contextList);
        final List<SummaryIvrTreeResponse> ivrTreeList = apiInterface.addIvrTreeList();
        model.addAttribute("ivrTreeList", ivrTreeList);
        final List<SummaryNumber070Response> number070List = apiInterface.addNumber070List();
        model.addAttribute("number070List", number070List);
        final List<SummarySoundListResponse> soundList = apiInterface.addSoundList();
        model.addAttribute("soundList", soundList);

        form.setParent(parent);
        return "admin/sounds/schedule/schedule-group/modal-schedule-item";
    }

    @GetMapping("{parent}/item/{child}/modal")
    public String modalScheduleItem(Model model, @ModelAttribute("form") ScheduleGroupListFormRequest form, @PathVariable Integer parent, @PathVariable Integer child) throws IOException, ResultFailException {
        final ScheduleGroupListDetailResponse entity = apiInterface.getItem(child);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return modalScheduleItem(model, form, parent);
    }
}
