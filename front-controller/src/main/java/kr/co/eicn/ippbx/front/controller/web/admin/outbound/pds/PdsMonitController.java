package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.model.enums.PDSGroupSpeedMultiple;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsMonitApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PDSExecuteListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PDSMonitResponse;
import kr.co.eicn.ippbx.model.search.PDSMonitSearchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/monit")
public class PdsMonitController extends BaseController {

    private final PdsMonitApiInterface apiInterface;
    private final QueueApiInterface    queueApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSMonitSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PDSMonitResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        model.addAttribute("pdsList", apiInterface.getPDSList(new PDSMonitSearchRequest()).stream().collect(Collectors.toMap(PDSExecuteListResponse::getLastExecuteId, PDSExecuteListResponse::getLastExecuteName)));
        model.addAttribute("pdsStatues", queueApiInterface.getPdsQueuePersonStatus());
        model.addAttribute("pdsGroupSpeedOptions", FormUtils.optionsOfCode(PDSGroupSpeedMultiple.class));

        return "admin/outbound/pds/monit/ground";
    }
}
