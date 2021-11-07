package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsUploadApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PDSUploadSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPDSGroupResponse;
import kr.co.eicn.ippbx.model.search.PDSUploadSearchRequest;
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
@RequestMapping("admin/outbound/pds/upload")
public class PdsUploadController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsUploadController.class);

    @Autowired
    private PdsUploadApiInterface apiInterface;
    @Autowired
    private SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSUploadSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PDSUploadSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Integer, String> pdsGroups = searchApiInterface.pdsGroup().stream().collect(Collectors.toMap(SearchPDSGroupResponse::getSeq, SearchPDSGroupResponse::getName));
        model.addAttribute("pdsGroups", pdsGroups);

        return "admin/outbound/pds/upload/ground";
    }

    @GetMapping("{uploadId}/modal")
    public String page(Model model, @PathVariable String uploadId) throws IOException, ResultFailException {
        final GroupUploadLogDetailResponse entity = apiInterface.uploadLog(uploadId);
        model.addAttribute("entity", entity);
        return "admin/application/maindb/upload/modal";
    }
}
