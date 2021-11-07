package kr.co.eicn.ippbx.front.controller.web.admin.monitor.display;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenConfigApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.ScreenConfigExpressionType;
import kr.co.eicn.ippbx.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.model.form.ScreenConfigFormRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
@LoginRequired
@Controller
@RequiredArgsConstructor
@RequestMapping("admin/monitor/screen/config")
public class ScreenConfigController extends BaseController {
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

    @Value("${eicn.screen.variation:false}")
    private Boolean variation;

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
        model.addAttribute("expressionTypes", variation
                ? FormUtils.options(false,
                ScreenConfigExpressionType.INTEGRATION_VARIATION,
                ScreenConfigExpressionType.BY_HUNT_VARIATION,
                ScreenConfigExpressionType.INBOUND_CHART,
                ScreenConfigExpressionType.LIST_CONSULTANT)
                : FormUtils.options(false,
                ScreenConfigExpressionType.INTEGRATION,
                ScreenConfigExpressionType.BY_HUNT,
                ScreenConfigExpressionType.BY_SERVICE));
        return "admin/monitor/screen/config/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ScreenConfigFormRequest form) throws IOException, ResultFailException {
        final ScreenConfigEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return modal(model, form);
    }
}
