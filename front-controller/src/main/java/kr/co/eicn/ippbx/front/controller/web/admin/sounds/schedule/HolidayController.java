package kr.co.eicn.ippbx.front.controller.web.admin.sounds.schedule;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.HolidayApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.HolyInfoResponse;
import kr.co.eicn.ippbx.model.form.HolyInfoFormRequest;
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
@RequestMapping("admin/sounds/schedule/holiday")
public class HolidayController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    @Autowired
    private HolidayApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<HolyInfoResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/sounds/schedule/holiday/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") HolyInfoFormRequest form) throws IOException, ResultFailException {
        return "admin/sounds/schedule/holiday/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") HolyInfoFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final HolyInfoResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        form.setHolyMonth(Integer.parseInt(entity.getHolyDate().split("-")[0]));
        form.setHolyDay(Integer.parseInt(entity.getHolyDate().split("-")[1]));

        return "admin/sounds/schedule/holiday/modal";
    }
}
