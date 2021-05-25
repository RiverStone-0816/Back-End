package kr.co.eicn.ippbx.front.controller.web.admin.acd.grade;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.grade.RouteAppApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.RouteApplicationResult;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.RouteApplicationType;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.RouteApplicationEntity;
import kr.co.eicn.ippbx.model.form.RAFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.RouteApplicationSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/acd/grade/route-app")
public class RouteAppController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RouteAppController.class);

    private final RouteAppApiInterface apiInterface;
    private final GradelistApiInterface gradelistApiInterface;

    public RouteAppController(RouteAppApiInterface apiInterface, GradelistApiInterface gradelistApiInterface) {
        this.apiInterface = apiInterface;
        this.gradelistApiInterface = gradelistApiInterface;
    }

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") RouteApplicationSearchRequest search) throws IOException, ResultFailException {
        final Pagination<RouteApplicationEntity> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> routeTypes = FormUtils.options(RouteApplicationType.class);
        model.addAttribute("routeTypes", routeTypes);

        final Map<String, String> resultTypes = FormUtils.options(RouteApplicationResult.class);
        model.addAttribute("resultTypes", resultTypes);

        return "admin/acd/grade/route-app/ground";
    }

    @GetMapping("{seq}/modal-accept")
    public String modalAccept(Model model, @ModelAttribute("form") RAFormUpdateRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final RouteApplicationEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        final Map<String, String> queues = gradelistApiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/acd/grade/route-app/modal-accept";
    }
}
