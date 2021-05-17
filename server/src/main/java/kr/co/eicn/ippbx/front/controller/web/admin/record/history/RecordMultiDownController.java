package kr.co.eicn.ippbx.front.controller.web.admin.record.history;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordMultiDownApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordDownSummaryResponse;
import kr.co.eicn.ippbx.server.model.search.RecordDownSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/record/history/multi-down")
public class RecordMultiDownController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordMultiDownController.class);

    @Autowired
    private RecordMultiDownApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") RecordDownSearchRequest search) throws IOException, ResultFailException {
        final List<RecordDownSummaryResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        return "admin/record/history/multi-down/ground";
    }
}
