package kr.co.eicn.ippbx.front.controller.web.admin.acd.route;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.CsRouteGetResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.CsRouteSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.server.model.form.CsRouteFormRequest;
import kr.co.eicn.ippbx.server.model.search.CsRouteSearchRequest;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/acd/route/cs")
public class CsRouteController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CsRouteController.class);

    @Autowired
    private CsRouteApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") CsRouteSearchRequest search) throws IOException, ResultFailException {
        final Pagination<CsRouteSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> queues = apiInterface.queue().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/acd/route/cs/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") CsRouteFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> queues = apiInterface.queue().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/acd/route/cs/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") CsRouteFormRequest form) throws IOException, ResultFailException {
        final CsRouteGetResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        final Map<String, String> queues = apiInterface.queue().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/acd/route/cs/modal";
    }
}
