package kr.co.eicn.ippbx.front.controller.web.admin.monitor.display;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenConfigApiInterface;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.jooq.eicn.enums.ScreenConfigExpressionType;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.server.model.form.ScreenConfigFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author tinywind
 */
@LoginRequired
@Controller
@AllArgsConstructor
@RequestMapping("admin/monitor/screen/config")
public class ScreenConfigController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ScreenConfigController.class);
    private static final Map<Integer, String> lookAndFeelToDescription;

    static {
        // FIXME: 관리 방법이 있어야 할 것같은데?
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "디자인1");
        map.put(2, "디자인2");
        map.put(3, "디자인3");
        lookAndFeelToDescription = Collections.unmodifiableMap(map);
    }

    private final ScreenConfigApiInterface apiInterface;

    @ModelAttribute("lookAndFeelToDescription")
    public Map<Integer, String> lookAndFeelToDescription() {
        return lookAndFeelToDescription;
    }

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<ScreenConfigEntity> list = apiInterface.list();
        model.addAttribute("list", list);
        return "admin/monitor/screen/config/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ScreenConfigFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> expressionTypes = FormUtils.options(false, ScreenConfigExpressionType.class);
        model.addAttribute("expressionTypes", expressionTypes);
        return "admin/monitor/screen/config/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ScreenConfigFormRequest form) throws IOException, ResultFailException {
        final ScreenConfigEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, String> expressionTypes = FormUtils.options(false, ScreenConfigExpressionType.class);
        model.addAttribute("expressionTypes", expressionTypes);
        return "admin/monitor/screen/config/modal";
    }
}
