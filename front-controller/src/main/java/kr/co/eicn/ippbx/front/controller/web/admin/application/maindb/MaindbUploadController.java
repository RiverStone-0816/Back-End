package kr.co.eicn.ippbx.front.controller.web.admin.application.maindb;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbUploadApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbUploadResponse;
import kr.co.eicn.ippbx.model.search.MaindbUploadSearchRequest;
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
@RequestMapping("admin/application/maindb/upload")
public class MaindbUploadController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbUploadController.class);

    @Autowired
    private MaindbUploadApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MaindbUploadSearchRequest search) throws IOException, ResultFailException {
        final Pagination<MaindbUploadResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        final Map<Integer, String> maindbTypes = apiInterface.maindbGroup().stream().collect(Collectors.toMap(GroupUploadLogResponse::getSeq, GroupUploadLogResponse::getName));
        model.addAttribute("maindbTypes", maindbTypes);

        return "admin/application/maindb/upload/ground";
    }

    @GetMapping("{uploadId}/modal")
    public String page(Model model, @PathVariable String uploadId) throws IOException, ResultFailException {
        final GroupUploadLogDetailResponse entity = apiInterface.uploadLog(uploadId);
        model.addAttribute("entity", entity);
        return "admin/application/maindb/upload/modal";
    }
}
