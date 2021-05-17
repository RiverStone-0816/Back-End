package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsMonitApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSExecuteListResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSMonitResponse;
import kr.co.eicn.ippbx.server.model.search.PDSMonitSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/monit")
public class PdsMonitController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsMonitController.class);

    private final PdsMonitApiInterface apiInterface;
    private final QueueApiInterface queueApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSMonitSearchRequest search) throws IOException, ResultFailException {
        model.addAttribute("pdsStatues", queueApiInterface.getPdsQueuePersonStatus());
        final Pagination<PDSMonitResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> pdsList = apiInterface.getPDSList(new PDSMonitSearchRequest()).stream().collect(Collectors.toMap(PDSExecuteListResponse::getLastExecuteId, PDSExecuteListResponse::getLastExecuteName));
        model.addAttribute("pdsList", pdsList);

        return "admin/outbound/pds/monit/ground";
    }
}
