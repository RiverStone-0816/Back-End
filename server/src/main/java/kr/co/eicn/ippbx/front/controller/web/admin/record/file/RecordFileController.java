package kr.co.eicn.ippbx.front.controller.web.admin.record.file;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.file.RecordFileApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.DiskResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.FileSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/record/file/file")
public class RecordFileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileController.class);

    @Autowired
    private RecordFileApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<FileSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);
        final DiskResponse disk = apiInterface.disk();
        model.addAttribute("disk", disk);

        return "admin/record/file/file/ground";
    }
}
